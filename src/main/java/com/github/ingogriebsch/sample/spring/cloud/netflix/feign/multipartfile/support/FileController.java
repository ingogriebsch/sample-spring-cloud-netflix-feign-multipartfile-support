/*
 * Copyright 2019 Ingo Griebsch
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at http://www.apache.org/licenses/LICENSE-2.0
 * Unless required by applicable law or agreed to in writing, software distributed under the License is distributed on an "AS IS"
 * BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and limitations under the License.
 */
package com.github.ingogriebsch.sample.spring.cloud.netflix.feign.multipartfile.support;

import static org.springframework.http.MediaType.MULTIPART_FORM_DATA_VALUE;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@RequiredArgsConstructor
@RestController
@Slf4j
public class FileController {

    static final String REQUEST_PART_FILE = "file";
    static final String REQUEST_PARAM_SINK = "sink";
    static final String PATH_INSERT = "/files";

    @NonNull
    private final FileClient fileClient;

    @PostMapping(path = PATH_INSERT, consumes = MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<Void> insert(@RequestPart(REQUEST_PART_FILE) MultipartFile file,
        @RequestParam(name = REQUEST_PARAM_SINK, required = false, defaultValue = "true") boolean sink) {
        if (sink) {
            log.info("Storing file '{}' somewhere...", file.getOriginalFilename());
        } else {
            log.info("Sending file '{}' to remote service...", file.getOriginalFilename());
            fileClient.insert(file);
        }
        return ResponseEntity.ok().build();
    }

}
