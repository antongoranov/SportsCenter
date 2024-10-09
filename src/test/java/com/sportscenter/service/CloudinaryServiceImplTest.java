package com.sportscenter.service;

import com.cloudinary.Cloudinary;
import com.cloudinary.Uploader;
import com.cloudinary.http44.UploaderStrategy;
import com.cloudinary.strategies.AbstractUploaderStrategy;
import com.sportscenter.service.impl.CloudinaryServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyMap;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class CloudinaryServiceImplTest {

    @Mock
    private Cloudinary cloudinaryMock;
    @Mock
    private Uploader uploaderMock;

    private MultipartFile testMultipartFile;

    @InjectMocks
    private CloudinaryServiceImpl cloudinaryServiceTest;

    @BeforeEach
    public void setUp() {
        testMultipartFile = new MockMultipartFile(
                        "fakeFile",
                        "test.jpg",
                        "image/jpeg",
                        new byte[0]);
    }

    //uploadUserProfilePicture
    @Test
    public void testUploadUserProfilePicture_success() throws IOException {
        String testUrl = "cloudinaryUrl.com/test.jpg";


        when(cloudinaryMock.uploader())
                .thenReturn(uploaderMock);

        when(uploaderMock.upload(any(Object.class), anyMap()))
                .thenReturn(Map.of("url", testUrl));

        String result = cloudinaryServiceTest.uploadUserProfilePicture(testMultipartFile);

        assertEquals(testUrl, result);
    }

    @Test
    public void testUploadUserProfilePicture_throws() throws IOException {
        when(cloudinaryMock.uploader())
                .thenReturn(uploaderMock);

        when(uploaderMock.upload(any(Object.class), anyMap()))
                .thenThrow(new IOException());

        assertThrows(IOException.class,
                () -> cloudinaryServiceTest.uploadUserProfilePicture(testMultipartFile));
    }

    //deleteUserOldProfilePicture
    @Test
    public void testDeleteUserOldProfilePicture_success() throws IOException {
        String publicIdTest = "1";
        when(cloudinaryMock.uploader())
                .thenReturn(uploaderMock);

        cloudinaryServiceTest.deleteUserOldProfilePicture(publicIdTest);

        verify(uploaderMock, times(1))
                .destroy(eq(publicIdTest), anyMap());
    }

    @Test
    public void testDeleteUserOldProfilePicture_throws() throws IOException {
        String publicIdTest = "1";
        when(cloudinaryMock.uploader())
                .thenReturn(uploaderMock);
        when(uploaderMock.destroy(eq(publicIdTest), anyMap()))
                .thenThrow(new IOException());

        assertThrows(IOException.class,
                () -> cloudinaryServiceTest.deleteUserOldProfilePicture(publicIdTest));
        
    }
}
