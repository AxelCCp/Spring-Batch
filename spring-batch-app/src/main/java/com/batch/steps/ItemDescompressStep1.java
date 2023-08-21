package com.batch.steps;

import lombok.extern.slf4j.Slf4j;
import org.springframework.batch.core.StepContribution;
import org.springframework.batch.core.scope.context.ChunkContext;
import org.springframework.batch.core.step.tasklet.Tasklet;
import org.springframework.batch.repeat.RepeatStatus;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.core.io.ResourceLoader;

import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.util.Enumeration;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;

@Slf4j
public class ItemDescompressStep1 implements Tasklet {


    @Override
    public RepeatStatus execute(StepContribution stepContribution, ChunkContext chunkContext) throws Exception {

        log.info("-----------------------Inicio del paso de descompresion------------------------");

        Resource resource = resourceLoader.getResource("classpath:files/persons.zip");                                       //se va a obtener el zip desde resources/files/person.zip.
        String filePath = resource.getFile().getAbsolutePath();                                                             //obtiene la ruta absoluta de donde se obtiene el archivo.
        ZipFile zipFile = new ZipFile(filePath);                                                                            //crea obj zipfile y le pasa la ruta.
        File destDir =  new File(resource.getFile().getParent(), "destination");                                            //obtiene el directorio padre de la ruta. el directorio "files". Y crea el directorio destination.
        if(!destDir.exists()){
            destDir.mkdir();
        }
        Enumeration<? extends ZipEntry> entries = zipFile.entries();                                                        //declara un enumerador y recibe en el , cualquier cosa q extienda de zipentry. en este caso toma tod0 lo que hay en el zipfile y lo considera como una entrada.
        while(entries.hasMoreElements()){
            ZipEntry zipEntry = entries.nextElement();
            File file = new File(destDir, zipEntry.getName());                                                              //en la rura destdir, se va a guardar el zipentry con el mismo nombre q venga.
            if(file.isDirectory()){                                                                                         //si el archivo comprimido es una carpeta, crea la carpeta.
                file.mkdir();
            }else{                                                                                                          //si no es  una carpeta, quiere decir que es un archivo
                InputStream inputStream = zipFile.getInputStream(zipEntry);                                                 //es archivo, se descomprime. se ontiene la secuencia de bites del archivo.
                FileOutputStream outputStream = new FileOutputStream(file);
                byte[]buffer = new byte[1024];                                                                              //se va a escribir el archivo por partes.  partes de 1024 bytes.

                //con esto se escribirian los archivos en el directorio.
                int length;
                while((length = inputStream.read(buffer)) > 0){                                                             //si es mayor q cero, se va a escribir el archivo.
                    outputStream.write(buffer, 0, length);                                                                  //se pasa el buffer, para q lo escriba en partes de 1024 bytes. se pasa un "0" para q parta en la posicion 0. se pasa el length, o sea los bytes q se estan leyendo desde el input stream.
                }

                outputStream.close();
                inputStream.close();
            }
        }
        zipFile.close();
        log.info("-----------------------Fin del paso de descompresion------------------------");
        return RepeatStatus.FINISHED;
    }

    @Autowired
    private ResourceLoader resourceLoader;
}
