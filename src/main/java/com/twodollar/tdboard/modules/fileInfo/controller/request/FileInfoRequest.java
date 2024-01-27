package com.twodollar.tdboard.modules.fileInfo.controller.request;

import com.twodollar.tdboard.modules.booking.entity.enums.BookingType;
import com.twodollar.tdboard.modules.fileInfo.controller.request.enums.UploadType;
import com.twodollar.tdboard.modules.fileInfo.entity.FileInfo;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@ToString
@Getter
@Setter
public class FileInfoRequest {

    private List<MultipartFile> files;
    private UploadType uploadType;

}
