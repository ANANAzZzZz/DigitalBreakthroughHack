package suai.vladislav.omskhack.controllers;

import com.aspose.slides.*;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;


import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.aspose.*;


import java.io.File;
import java.io.IOException;
import java.util.Iterator;
import java.util.Map;

@RestController
@RequiredArgsConstructor
public class presentationController {
    @GetMapping("/presentation")
    public String createPresentation() {

         String jsonFilePath = "data.json"; // Укажите путь к вашему JSON файлу

        // Создание новой презентации
        Presentation presentation = new Presentation();

        try {
            // Чтение и парсинг JSON с использованием Jackson
            ObjectMapper objectMapper = new ObjectMapper();
            JsonNode rootNode = objectMapper.readTree(new File(jsonFilePath));

            // Создание слайдов на основе данных JSON
            createSlidesFromJson(rootNode, presentation);

            // Сохранение презентации в файл
            presentation.save("output.pptx", SaveFormat.Pptx);
            System.out.println("Презентация успешно сохранена как output.pptx");

        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            presentation.dispose();
        }
        return "test";
    }

    private static void createSlidesFromJson(JsonNode rootNode, Presentation presentation) {
        if (rootNode.isObject()) {
            // Если JSON корень - объект, перебираем ключи и значения
            Iterator<Map.Entry<String, JsonNode>> fields = rootNode.fields();

            while (fields.hasNext()) {
                Map.Entry<String, JsonNode> field = fields.next();
                String key = field.getKey();
                JsonNode value = field.getValue();

                // Создаем новый слайд
                ISlide slide = presentation.getSlides().addEmptySlide(presentation.getLayoutSlides().get_Item(0));

                // Добавляем заголовок с ключом JSON
                IAutoShape titleShape = slide.getShapes().addAutoShape(ShapeType.Rectangle, 50, 50, 600, 50);
                titleShape.getTextFrame().setText(key);

                // Добавляем содержание в зависимости от типа значения
                if (value.isObject()) {
                    // Если значение - объект, добавляем его содержимое
                    addTextFromJsonObject(value, slide);
                } else if (value.isArray()) {
                    // Если значение - массив, добавляем элементы массива
                    addTextFromJsonArray(value, slide);
                } else {
                    // Если значение - простой тип (строка, число и т.д.)
                    IAutoShape contentShape = slide.getShapes().addAutoShape(ShapeType.Rectangle, 50, 150, 600, 300);
                    contentShape.getTextFrame().setText(value.asText());
                }
            }
        }
    }

    private static void addTextFromJsonObject(JsonNode jsonObject, ISlide slide) {
        StringBuilder content = new StringBuilder();
        Iterator<Map.Entry<String, JsonNode>> fields = jsonObject.fields();

        // Перебираем ключи и значения внутри объекта и добавляем их в текст
        while (fields.hasNext()) {
            Map.Entry<String, JsonNode> field = fields.next();
            content.append(field.getKey()).append(": ").append(field.getValue().asText()).append("\n");
        }

        // Добавляем текст в слайд
        IAutoShape contentShape = slide.getShapes().addAutoShape(ShapeType.Rectangle, 50, 150, 600, 300);
        contentShape.getTextFrame().setText(content.toString());
    }

    private static void addTextFromJsonArray(JsonNode jsonArray, ISlide slide) {
        StringBuilder content = new StringBuilder();

        // Перебираем элементы массива и добавляем их в текст
        for (JsonNode element : jsonArray) {
            content.append(element.asText()).append("\n");
        }

        // Добавляем текст в слайд
        IAutoShape contentShape = slide.getShapes().addAutoShape(ShapeType.Rectangle, 50, 150, 600, 300);
        contentShape.getTextFrame().setText(content.toString());
    }
}

