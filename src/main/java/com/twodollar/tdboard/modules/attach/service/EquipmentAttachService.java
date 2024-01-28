package com.twodollar.tdboard.modules.attach.service;

import com.twodollar.tdboard.modules.attach.entity.BoardAttach;
import com.twodollar.tdboard.modules.attach.entity.EquipmentAttach;
import com.twodollar.tdboard.modules.attach.repository.BoardAttachRepository;
import com.twodollar.tdboard.modules.attach.repository.EquipmentAttachRepository;
import com.twodollar.tdboard.modules.board.entity.Board;
import com.twodollar.tdboard.modules.board.service.BoardService;
import com.twodollar.tdboard.modules.equipment.entity.Equipment;
import com.twodollar.tdboard.modules.equipment.service.EquipmentService;
import com.twodollar.tdboard.modules.fileInfo.entity.FileInfo;
import com.twodollar.tdboard.modules.fileInfo.service.FileInfoService;
import com.twodollar.tdboard.modules.fileInfo.util.FileUtils;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import javax.transaction.Transactional;
import java.util.List;
import java.util.stream.Collectors;

@Service
@Slf4j
@RequiredArgsConstructor
public class EquipmentAttachService {
    private final EquipmentAttachRepository equipmentAttachRepository;
    private final FileInfoService fileInfoService;
    private final EquipmentService equipmentService;
    private final FileUtils fileUtils;

    public List<FileInfo> getAttaches(Long equipmentId) {
        Equipment equipment = equipmentService.getEquipmentById(equipmentId);
        List<EquipmentAttach> list = equipmentAttachRepository.getEquipmentAttachesByEquipment(equipment).orElseThrow((() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "등록된 첨부파일이 없습니다.")));

        List<FileInfo> fileList = list.stream().map(equipmentAttach -> equipmentAttach.getFileInfo()).collect(Collectors.toList());
        return fileList;
    }

    public List<EquipmentAttach> createAttach(final Long equipmentId, List<Long> files) {
        Equipment equipment = equipmentService.getEquipmentById(equipmentId);
        List<EquipmentAttach> attachList = files.stream().map(fileInfoId -> this.createAttach(equipment, fileInfoId)).collect(Collectors.toList());
        return attachList;
    }

    public EquipmentAttach createAttach(final Equipment equipment, Long fileInfoId) {
        FileInfo fileInfo = fileInfoService.getFileInfoById(fileInfoId);
        EquipmentAttach equipmentAttach = new EquipmentAttach(equipment,fileInfo);
        return equipmentAttachRepository.save(equipmentAttach);
    }

    @Transactional(rollbackOn = Exception.class)
    public List<EquipmentAttach> createUpdate(final Long equipmentId, List<Long> files) {
        Equipment equipment = equipmentService.getEquipmentById(equipmentId);

        // 0. 기존 파일첨부리스트 조회
        List<EquipmentAttach> tempList = equipmentAttachRepository.getEquipmentAttachesByEquipment(equipment).orElse(null);

        // 1. 삭제
        equipmentAttachRepository.deleteEquipmentAttachesByEquipment(equipment);

        // 2. 등록
        List<EquipmentAttach> attachList = this.createAttach(equipmentId, files);

        // 3. 파일삭제 처리
        List<Long> newFileList = attachList.stream().map(equipmentAttach -> equipmentAttach.getFileInfo().getId()).collect(Collectors.toList());
        List<FileInfo> dellist = tempList.stream()
                .filter(temp -> {
                    boolean isNotExist = !newFileList.contains(temp.getFileInfo().getId());
                    return isNotExist;
                })
                .map(temp -> temp.getFileInfo())
                .collect(Collectors.toList());

        dellist.stream().forEach(fileInfo -> {
            boolean isDeleted = fileUtils.deleteFile(fileInfo.getStoredPath());
            fileInfoService.deleteFileInfoById(fileInfo.getId());
            log.info("isDeleted : {}", isDeleted);
        });


        return attachList;
    }
}
