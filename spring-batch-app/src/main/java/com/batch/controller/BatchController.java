package com.batch.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.JobParameters;
import org.springframework.batch.core.JobParametersBuilder;
import org.springframework.batch.core.launch.JobLauncher;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

@Slf4j
@RestController
@RequestMapping("/v1")
public class BatchController {

    @PostMapping("/uploadFile")
    public ResponseEntity<?>receiveFile(@RequestParam(name="file") MultipartFile multipartFile){

        String fileName = multipartFile.getOriginalFilename();                                                                                                      //obtiene el nombre del archivo.
            try{
                Path path = Paths.get("src" + File.separator + "main" + File.separator + "resources" + File.separator + "files" + File.separator + fileName);       //se escribe el archivo en la carpeta resource.
                Files.createDirectories(path.getParent());                                                                                                          //si el directorio no existe, lo crea.
                Files.copy(multipartFile.getInputStream(), path, StandardCopyOption.REPLACE_EXISTING);                                                              //escribe el archivo en una secuencia de bytes, en la ruta path, si el archivo ya existe, lo reemplaza.

                log.info("------------inicio del proceso batch------------");
                JobParameters jobParameters = new JobParametersBuilder()                                                                                            //todos los jobs deben ser diferenciados a través de sus atributos. siempre q quieras diferenciar un job de otro, deben tener atributos diferentes.
                                                    //estos son argumentos q se van a mandar al contexto de spring batch
                                                    .addDate("fecha", new Date())                                                                                   // con AddDate() se le pone un identificador al proceso batch.
                                                    .addString("fileName", fileName)
                                                    .toJobParameters();

                //se usa un Job laucher para ejecutar el job
                jobLauncher.run(job, jobParameters);


                Map<String, String> response = new HashMap<>();
                response.put("archivo", fileName);
                response.put("estado", "recibido");
                return ResponseEntity.ok(response);
            }catch (Exception e){
                log.error("Error al iniciar el proceso batch. Error: {}", e.getMessage());
                throw  new RuntimeException();
            }
    }

    @Autowired
    private JobLauncher jobLauncher;
    @Autowired
    private Job job;
}
