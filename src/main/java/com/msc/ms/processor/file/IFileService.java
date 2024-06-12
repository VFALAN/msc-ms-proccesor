package com.msc.ms.processor.file;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.core.io.Resource;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

@FeignClient(url = "${msc.services.minio.url}", name = "msc-ms-file")
public interface IFileService {

    @GetMapping("/file/getFile")
    ResponseEntity<Resource> getFile(@RequestParam("bucket") String pBucket, @RequestParam("file") String pFile);

    @GetMapping("/file/getFilesNamesFromBucket/{bucketName}")
    ResponseEntity<List<String>> getFilesNamesFromBucket(@PathVariable(name = "bucketName") String pBucketName);
}
