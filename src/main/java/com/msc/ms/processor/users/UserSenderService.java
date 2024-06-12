package com.msc.ms.processor.users;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.msc.ms.processor.file.IFileService;
import io.micrometer.core.annotation.Counted;
import io.micrometer.core.annotation.Timed;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.stream.Collectors;

@Service
@Slf4j
@RequiredArgsConstructor
public class UserSenderService {
    @Value("${minio.bucket.process}")
    private String bucket;
    @Value("${msc.queue.users}")
    private String queue;
    private final IFileService iFileService;
    private final RabbitTemplate rabbitTemplate;
    private final ObjectMapper objectMapper;
    private static final Integer ID_PROSPECT = 2;
    private static final String UNSIGNED_DATA = "UNSIGNED-DATA";

    @Timed("process.files.users")
    public void proccesUsersFiles() {
        log.info("Stating the process");
        final var filesResponse = this.iFileService.getFilesNamesFromBucket(this.bucket);
        if (filesResponse.getStatusCode().equals(HttpStatus.OK)) {
            final var listOfFiles = filesResponse.getBody();
            if (!listOfFiles.isEmpty()) {
                log.info("has been founded {} files",listOfFiles.size());
                listOfFiles.forEach(fileName -> {
                    final var file = getFile(fileName);
                    try {
                        final var jsonStringFileContent = convertResourceToJsonString(file.getInputStream());
                        final var parsedData = objectMapper.readValue(jsonStringFileContent, new TypeReference<List<UserJsonDTO>>() {
                        });
                        log.info("starting process for file {} with data size of {} items",file.getFile().getName(),parsedData.size());
                        parsedData.forEach(this::processUser);
                    } catch (IOException e) {
                        log.error("error getting inputStream for: {} in the bucket: {}", fileName, this.bucket);
                        throw new RuntimeException(e);
                    }
                });
            }
        }
    }

    @Counted("process.files.users.process.user")
    private void processUser(UserJsonDTO userJsonDTO) {
        CompletableFuture.supplyAsync(() -> {
            final var objectToSend = castUserJsonDTOToUserRequest(userJsonDTO);
            this.rabbitTemplate.convertAndSend(this.queue, objectToSend);
            return true;
        }).whenComplete((result, error) -> {
            if (error != null) {
                log.error("error in send of information ue: {}", error.getMessage());
            } else {
                log.info("information to user :{} send successful", userJsonDTO.getCorreo());
            }
        });
    }

    private UserRequestDTO castUserJsonDTOToUserRequest(UserJsonDTO userJsonDTO) {
        final var sdf = new SimpleDateFormat("yyyy-dd-MM");
        return UserRequestDTO.builder()
                .age(userJsonDTO.getEdad())
                .email(userJsonDTO.getCorreo())
                .userName(userJsonDTO.getCurp().concat(sdf.format(new Date()))) //pendign to know how to get
                .name(userJsonDTO.getNombre())
                .idLocation(userJsonDTO.getLocalidadId())
                .birthDate(userJsonDTO.getFechaNacimiento())
                .lastName(userJsonDTO.getApellidoPaterno())
                .middleName(userJsonDTO.getApellidoMaterno())
                .description(UNSIGNED_DATA)
                .phoneNumber(userJsonDTO.getTelefono())
                .street(UNSIGNED_DATA)
                .number(UNSIGNED_DATA)
                .idProfile(ID_PROSPECT)
                .build();
    }

    @Timed("process.files.users.get.file")
    private Resource getFile(String file) {
        final var responseFile = iFileService.getFile(this.bucket, file);
        if (responseFile.getStatusCode().equals(HttpStatus.OK)) {
            return responseFile.getBody();
        }
        return null;
    }


    private String convertResourceToJsonString(InputStream inputStream) {
        final var bufferedReader = new BufferedReader(new InputStreamReader(inputStream));
        return bufferedReader.lines().collect(Collectors.joining(System.lineSeparator()));
    }
}
