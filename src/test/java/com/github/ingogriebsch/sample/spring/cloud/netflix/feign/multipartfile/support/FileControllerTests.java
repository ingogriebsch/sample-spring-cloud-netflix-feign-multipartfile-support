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

import static com.github.ingogriebsch.sample.spring.cloud.netflix.feign.multipartfile.support.FileController.PATH_INSERT;
import static com.github.ingogriebsch.sample.spring.cloud.netflix.feign.multipartfile.support.FileController.REQUEST_PARAM_SINK;
import static com.github.ingogriebsch.sample.spring.cloud.netflix.feign.multipartfile.support.FileController.REQUEST_PART_FILE;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoMoreInteractions;
import static org.springframework.http.MediaType.APPLICATION_JSON_UTF8;
import static org.springframework.http.MediaType.MULTIPART_FORM_DATA_VALUE;
import static org.springframework.http.MediaType.TEXT_PLAIN_VALUE;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.fileUpload;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;

@RunWith(SpringRunner.class)
@WebMvcTest(FileController.class)
public class FileControllerTests {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private FileClient fileClient;

    @Test
    public void insert_should_return_bad_request_if_request_body_is_missing() throws Exception {
        ResultActions actions = mockMvc.perform(post(PATH_INSERT).contentType(MULTIPART_FORM_DATA_VALUE));
        actions.andExpect(status().isBadRequest());

        verifyNoMoreInteractions(fileClient);
    }

    @Test
    public void insert_should_return_ok_if_file_is_available_in_request_body() throws Exception {
        MockMultipartFile file =
            new MockMultipartFile(REQUEST_PART_FILE, "data.txt", TEXT_PLAIN_VALUE, "Welcome stranger!".getBytes());

        ResultActions actions = mockMvc.perform(fileUpload(PATH_INSERT).file(file));
        actions.andExpect(status().isOk());

        verifyNoMoreInteractions(fileClient);
    }

    @Test
    public void insert_should_return_unsupported_media_type_if_content_type_is_not_matching() throws Exception {
        ResultActions actions = mockMvc.perform(post(PATH_INSERT).contentType(APPLICATION_JSON_UTF8));
        actions.andExpect(status().isUnsupportedMediaType());

        verifyNoMoreInteractions(fileClient);
    }

    @Test
    public void insert_should_forward_file_if_request_parameter_sink_is_set_to_false() throws Exception {
        MockMultipartFile file =
            new MockMultipartFile(REQUEST_PART_FILE, "data.txt", TEXT_PLAIN_VALUE, "Welcome stranger!".getBytes());

        ResultActions actions =
            mockMvc.perform(fileUpload(PATH_INSERT).file(file).param(REQUEST_PARAM_SINK, Boolean.FALSE.toString()));
        actions.andExpect(status().isOk());

        verify(fileClient, times(1)).insert(file);
    }
}
