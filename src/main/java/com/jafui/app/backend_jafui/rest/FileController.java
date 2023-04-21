package com.jafui.app.backend_jafui.rest;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

@RestController
public class FileController {

    @PostMapping("/upload")
    public ResponseEntity<String> uploadFile(@RequestParam("file") MultipartFile file) {
        try {
            // Verifica se o arquivo é nulo ou vazio
            if (file.isEmpty()) {
                return ResponseEntity.badRequest().body("O arquivo não pode estar vazio!");
            }

            // Salva o arquivo em um diretório temporário
            Path tempDir = Files.createTempDirectory("");
            Path filePath = Paths.get(tempDir.toString(), file.getOriginalFilename());
            Files.write(filePath, file.getBytes());

            // Retorna a mensagem de sucesso
            return ResponseEntity.ok("Arquivo enviado com sucesso e salvo em " + filePath.toString());
        } catch (IOException e) {
            // Retorna o status HTTP 500 (INTERNAL SERVER ERROR) em caso de erro
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Erro ao enviar o arquivo: " + e.getMessage());
        }
    }
}
