package online.referity.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import online.referity.dto.response.ChatCompletion;
import online.referity.dto.response.PDFResponse;
import online.referity.utils.ResponseHandler;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.text.PDFTextStripper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.*;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.multipart.MultipartFile;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@RestController
@SecurityRequirement(name = "api")
@CrossOrigin("*")
public class PDFController {

    @Autowired
    RestTemplate restTemplate;

    @Autowired
    ResponseHandler responseHandler;

    @PostMapping(path="pdf", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity pdfScan(@RequestParam("file") MultipartFile file){
        try{
            PDDocument document = PDDocument.load(file.getInputStream());
            PDFTextStripper textStripper = new PDFTextStripper();
            String pdfText = textStripper.getText(document);
            document.close();

            String url = "https://api.openai.com/v1/chat/completions";

            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_JSON);
            headers.setBearerAuth("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");

            Map<String, Object> requestBody = new HashMap<>();
            requestBody.put("model", "gpt-3.5-turbo-0301");

            List<Map<String, String>> messages = new ArrayList<>();
            Map<String, String> systemMessage = new HashMap<>();
            systemMessage.put("role", "system");
            systemMessage.put("content" ,"{\n" +
                    "    \"jobTitle\": \"\",\n" +
                    "    \"fullName\": \"\",\n" +
                    "    \"email\": \"\",\n" +
                    "    \"address\": \"\",\n" +
                    "    \"phone\": \"\",\n" +
                    "    \"linkedInLink\": \"\",\n" +
                    "    \"facebookLink\": \"\",\n" +
                    "    \"githubLink\": \"\",\n" +
                    "    \"education\": \"\",\n" +
                    "    \"summary\": \"\",\n" +
                    "    \"certificationRequestList\": [\n" +
                    "        {\n" +
                    "            \"certificationName\": \"\",\n" +
                    "            \"organization\": \"\",\n" +
                    "            \"certificationUrl\": \"\",\n" +
                    "            \"dateFrom\": \"2023-08-02T12:47:44.408Z\",\n" +
                    "            \"dateTo\": \"2023-08-18T12:47:46.500Z\"\n" +
                    "        }\n" +
                    "    ],\n" +
                    "    \"experienceRequestList\": [\n" +
                    "        {\n" +
                    "            \"company\": \"\",\n" +
                    "            \"jobTitle\": \"\",\n" +
                    "            \"dateFrom\": \"2023-08-01T12:48:00.107Z\",\n" +
                    "            \"dateTo\": \"2023-08-18T12:48:01.546Z\",\n" +
                    "            \"jobDescription\": \"\"\n" +
                    "        }\n" +
                    "    ],\n" +
                    "    \"skill\": [\n" +
                    "        {\n" +
                    "            \"name\": \"1\",\n" +
                    "            \"value\": 50\n" +
                    "        }\n" +
                    "    ],\n" +
                    "    \"languageRequests\": [\n" +
                    "        {\n" +
                    "            \"LanguageId\": 0,\n" +
                    "            \"ponit\": 0\n" +
                    "        }\n" +
                    "    ]\n" +
                    "}" );
            messages.add(systemMessage);

            Map<String, String> message = new HashMap<>();
            message.put("role", "user");


            message.put("content", pdfText +
                    "\nread this profile convert to my struct (if mention). all of this show the original of CV if mention. If not mention invoke [not mention]." +
                    " Get all the information that you can get. If not mention can give me the empty array flow the template of system .Read  faster please!!! \n" );
            messages.add(message);
            requestBody.put("messages", messages);

            HttpEntity<Map<String, Object>> requestEntity = new HttpEntity<>(requestBody, headers);

            ResponseEntity<String> response = restTemplate.exchange(url, HttpMethod.POST, requestEntity, String.class);
            // parse JSON response to object
            ObjectMapper objectMapper = new ObjectMapper();
            ChatCompletion chatCompletion = objectMapper.readValue(response.getBody(), ChatCompletion.class);
            PDFResponse pdfResponse = new PDFResponse();
            System.out.println(chatCompletion.getChoices().get(0).getMessage().getContent());
            String[] content = chatCompletion.getChoices().get(0).getMessage().getContent().split("\n");
//            for(String str: content){
//                String[] arr = str.split(":");
//                switch (arr[0]){
//                    case "Fullname":
//                        pdfResponse.setFullName(arr[1].trim());
//                        break;
//                    case "Job Title":
//                        pdfResponse.setJobTitle(arr[1].trim());
//                        break;
//                    case "Phone":
//                        pdfResponse.setPhone(arr[1].trim());
//                        break;
//                    case "Address":
//                        pdfResponse.setAddress(arr[1].trim());
//                        break;
//                    case "Year of experience":
//                        pdfResponse.setYearOfExperience(arr[1].trim());
//                        break;
//                    case "Summary":
//                        pdfResponse.setSummary(arr[1].trim());
//                        break;
//                    case "Skill":
//                        pdfResponse.setSkills(arr[1].trim());
//                        break;
//                    case "University":
//                        pdfResponse.setEducation(arr[1].trim());
//                        break;
//                    case "GPA":
//                        pdfResponse.setGpa(arr[1].trim());
//                        break;
//                    case "TemplateEmail":
//                        pdfResponse.setEmail(arr[1].trim());
//                        break;
//                    case "Facebook Link":
//                        pdfResponse.setFacebookLink(arr[1].trim());
//                        break;
//                    case "Github Link":
//                        pdfResponse.setGithubLink(arr[1].trim());
//                        break;
//                    case "LinkedIn Link":
//                        pdfResponse.setLinkedInLink(arr[1].trim());
//                        break;
//                }
//            }
            return responseHandler.response(200,"Scan CV success!", chatCompletion.getChoices().get(0).getMessage().getContent());
        }catch (Exception e){
            e.printStackTrace();
            System.out.println(e.getMessage());
        }
        return null;
    }

    @PostMapping("convert-pdf")
    public ResponseEntity convertPDFToText(@RequestParam("file") MultipartFile file){
        try{
            PDDocument document = PDDocument.load(file.getInputStream());
            PDFTextStripper textStripper = new PDFTextStripper();
            String pdfText = textStripper.getText(document);
            return responseHandler.response(200,"Scan CV success!", pdfText);
        }catch (Exception e){
            throw new RuntimeException(e.getMessage());
        }
    }
}
