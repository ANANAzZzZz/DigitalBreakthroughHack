package suai.vladislav.omskhack.controllers;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.apache.poi.sl.usermodel.PictureData;
import org.apache.poi.xslf.usermodel.*;

import org.springframework.core.io.InputStreamResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import suai.vladislav.omskhack.dto.Response;

import java.awt.*;
import java.io.*;
import java.util.Base64;


@RestController
@RequiredArgsConstructor
public class presentationController {
    @PostMapping("/presentation")
    public ResponseEntity<?> createPresentation(@RequestBody JsonNode rootNode) {
        try {
            // Создание презентации
            XMLSlideShow ppt = new XMLSlideShow();

            // Установка заголовка презентации
            String presentationTitle = rootNode.path("title").asText();

            // Проход по слайдам
            for (JsonNode slideNode : rootNode.path("slides")) {
                String slideTitle = slideNode.path("title").asText();

                // Создание нового слайда
                XSLFSlide slide = ppt.createSlide();

                // Добавление заголовка слайда
                XSLFTextBox titleBox = slide.createTextBox();
                titleBox.setAnchor(new Rectangle(50, 20, 600, 50));
                XSLFTextParagraph titleParagraph = titleBox.addNewTextParagraph();
                XSLFTextRun titleRun = titleParagraph.addNewTextRun();
                titleRun.setText(slideTitle);
                titleRun.setFontSize(28.0);

                // Проход по элементам слайда
                for (JsonNode elementNode : slideNode.path("elements")) {
                    String elementType = elementNode.path("type").asText();

                    switch (elementType) {
                        case "text":
                            // Добавление текстового элемента
                            String content = elementNode.path("content").asText();
                            JsonNode position = elementNode.path("position");
                            double fontSize = elementNode.path("fontSize").asDouble(18.0);

                            XSLFTextBox textBox = slide.createTextBox();
                            textBox.setAnchor(new Rectangle(
                                    position.path("x").asInt(),
                                    position.path("y").asInt(),
                                    position.path("width").asInt(),
                                    position.path("height").asInt()
                            ));
                            XSLFTextParagraph textParagraph = textBox.addNewTextParagraph();
                            XSLFTextRun textRun = textParagraph.addNewTextRun();
                            textRun.setText(content);
                            textRun.setFontSize(fontSize);
                            break;

                        case "image":
                            // Добавление изображения из Base64
                            String imageBase64 = elementNode.path("src").asText(); // В JSON вместо пути хранится Base64 строка
                            JsonNode imagePosition = elementNode.path("position");

                            try {
                                // Декодирование Base64 строки в массив байтов
                                byte[] imageBytes = Base64.getDecoder().decode(imageBase64);

                                // Создание InputStream из массива байтов
                                ByteArrayInputStream imageInputStream = new ByteArrayInputStream(imageBytes);

                                // Определение типа изображения (JPEG используется как пример, можно адаптировать под нужный формат)
                                XSLFPictureData pictureData = ppt.addPicture(imageInputStream, PictureData.PictureType.JPEG);

                                // Добавление изображения на слайд
                                XSLFPictureShape pictureShape = slide.createPicture(pictureData);
                                pictureShape.setAnchor(new Rectangle(
                                        imagePosition.path("x").asInt(),
                                        imagePosition.path("y").asInt(),
                                        imagePosition.path("width").asInt(),
                                        imagePosition.path("height").asInt()
                                ));
                            } catch (IllegalArgumentException e) {
                                // Обработка ошибки декодирования Base64
                                System.out.println("Ошибка при декодировании изображения из Base64.");
                                e.printStackTrace();
                            } catch (IOException e) {
                                // Обработка ошибки при добавлении изображения в презентацию
                                System.out.println("Ошибка при добавлении изображения в презентацию.");
                                e.printStackTrace();
                            }
                            break;

                        case "table":
                            // Добавление таблицы
                            JsonNode tableHeaders = elementNode.path("headers");
                            JsonNode tableRows = elementNode.path("rows");
                            JsonNode tablePosition = elementNode.path("position");

                            int numColumns = tableHeaders.size();
                            int numRows = tableRows.size() + 1; // +1 для заголовка

                            XSLFTable table = slide.createTable();
                            table.setAnchor(new Rectangle(
                                    tablePosition.path("x").asInt(),
                                    tablePosition.path("y").asInt(),
                                    tablePosition.path("width").asInt(),
                                    tablePosition.path("height").asInt()
                            ));

                            // Добавление заголовка таблицы
                            XSLFTableRow headerRow = table.addRow();
                            for (JsonNode header : tableHeaders) {
                                XSLFTableCell cell = headerRow.addCell();
                                cell.setText(header.asText());
                                cell.setFillColor(new Color(0, 120, 215));
                                cell.setFillColor(Color.WHITE);
                            }

                            // Добавление строк таблицы
                            for (JsonNode row : tableRows) {
                                XSLFTableRow tableRow = table.addRow();
                                for (JsonNode cellValue : row) {
                                    XSLFTableCell cell = tableRow.addCell();
                                    cell.setText(cellValue.asText());
                                }
                            }
                            break;

                        default:
                            System.out.println("Неизвестный тип элемента: " + elementType);
                    }
                }
            }

            // Сохранение презентации
            try (FileOutputStream out = new FileOutputStream("complex_presentation.pptx")) {
                ppt.write(out);
            }

            System.out.println("Комплексная презентация успешно создана!");

        } catch (IOException e) {
            e.printStackTrace();
        }
        return new ResponseEntity<>("Презентация успешно сгенерирована и доступна для скачивания", HttpStatus.OK);
    }

    @GetMapping("/presentation")
    public ResponseEntity<InputStreamResource> downloadFile() throws IOException {
        // Путь к файлу
        File file = new File("complex_presentation.pptx");

        if (!file.exists()) {
            return ResponseEntity.notFound().build(); // Возвращает 404, если файл не найден
        }

        // Создание InputStreamResource для передачи файла в ответе
        InputStreamResource resource = new InputStreamResource(new FileInputStream(file));

        // Настройка заголовков ответа для корректного скачивания файла
        return ResponseEntity.ok()
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + file.getName() + "\"")
                .contentType(MediaType.APPLICATION_OCTET_STREAM) // Устанавливает тип содержимого
                .contentLength(file.length())
                .body(resource);
    }
}
