/*
 * Copyright 2018 Ingo Griebsch
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at http://www.apache.org/licenses/LICENSE-2.0
 * Unless required by applicable law or agreed to in writing, software distributed under the License is distributed on an "AS IS"
 * BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and limitations under the License.
 */
package com.github.ingogriebsch.sample.spring.cloud.netflix.feign.multipartfile.support;

import static com.github.ingogriebsch.sample.spring.cloud.netflix.feign.multipartfile.support.FileController.PATH_INSERT;
import static org.springframework.http.MediaType.MULTIPART_FORM_DATA_VALUE;
import static org.springframework.web.bind.annotation.RequestMethod.POST;

import org.springframework.cloud.netflix.feign.FeignClient;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.multipart.MultipartFile;

@FeignClient(name = "file-client", url = "${file-client.url}", configuration = FeignClientConfiguration.class)
public interface FileClient {

    @RequestMapping(path = PATH_INSERT, method = POST, consumes = MULTIPART_FORM_DATA_VALUE)
    void insert(@RequestPart("file") MultipartFile file);
}
