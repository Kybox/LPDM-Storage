package com.lpdm.msstorage.controller;

import com.lpdm.msstorage.dao.StorageRepository;
import com.lpdm.msstorage.entity.Storage;
import com.lpdm.msstorage.exception.FileNotFoundException;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Optional;

@RefreshScope
@RequestMapping("/user")
@RestController
public class FileController {

    private Logger log = LogManager.getLogger(this.getClass());
    private final StorageRepository storageRepository;

    @Autowired
    public FileController(StorageRepository storageRepository) {
        this.storageRepository = storageRepository;
    }

    @GetMapping("/{id}")
    public ResponseEntity<List<Storage>> getFilesByOwner(@PathVariable int id){

        List<Storage> storageList = storageRepository.findAllByOwner(id);
        if (storageList.isEmpty()) throw new FileNotFoundException();
        return ResponseEntity.ok().body(storageList);
    }

    @GetMapping("/{id}/delete/{folder}/{file}")
    public ResponseEntity<List<Storage>> deleteFile(@PathVariable(name = "id") int id,
                                                    @PathVariable(name = "folder") String folder,
                                                    @PathVariable(name = "file") String file){

        String url = "https://files.lpdm.kybox.fr/" + folder + "/" + file;
        log.info("File to delete : " + url);
        Optional<Storage> storage = storageRepository.findByOwnerAndUrl(id, url);
        if(!storage.isPresent()) throw new FileNotFoundException();

        storageRepository.delete(storage.get());

        List<Storage> storageList = storageRepository.findAllByOwner(id);
        return ResponseEntity.ok(storageList);
    }
}
