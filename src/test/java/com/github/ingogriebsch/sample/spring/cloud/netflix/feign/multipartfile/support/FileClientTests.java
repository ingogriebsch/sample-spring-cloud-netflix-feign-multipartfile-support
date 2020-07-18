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

import static com.github.ingogriebsch.sample.spring.cloud.netflix.feign.multipartfile.support.FileController.REQUEST_PART_FILE;
import static org.apache.commons.lang3.RandomStringUtils.randomAlphabetic;
import static org.springframework.boot.test.context.SpringBootTest.WebEnvironment.DEFINED_PORT;

import feign.FeignException;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;

@RunWith(SpringRunner.class)
@ActiveProfiles(value = "feignclient")
@SpringBootTest(classes = Application.class, webEnvironment = DEFINED_PORT)
public class FileClientTests {

    @Autowired
    private FileClient fileClient;

    @Test
    public void insert_should_succeed_if_file_is_available_in_request_body() throws Exception {
        fileClient.insert(new MockMultipartFile(REQUEST_PART_FILE, "Welcome stranger!".getBytes()));
    }

    @Test(expected = FeignException.class)
    public void insert_should_throw_exception_if_request_part_name_is_not_matching() throws Exception {
        fileClient.insert(new MockMultipartFile(randomAlphabetic(8), "Welcome stranger!".getBytes()));
    }

    @Test(expected = IllegalArgumentException.class)
    public void insert_should_throw_exception_if_file_is_not_available_in_request_body() throws Exception {
        fileClient.insert(null);
    }
}
