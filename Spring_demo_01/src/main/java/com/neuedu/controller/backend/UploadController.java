package com.neuedu.controller.backend;
import com.neuedu.common.ResponseCode;
import com.neuedu.common.ServeResponse;
import com.neuedu.vo.ImageVO;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import java.io.File;
import java.io.IOException;
import java.util.UUID;

@Controller
@RequestMapping("/manage/")
public class UploadController {

    @Value("${shopping.imageHost}")
    private String imageHost;
    @GetMapping(value = "/upload")
    public String upload(){

        return "upload";
    }
    @PostMapping(value = "/upload")
    @ResponseBody
    public ServeResponse upload(@RequestParam("uploadfile")MultipartFile uploadfile){
        if (uploadfile == null || uploadfile.getOriginalFilename().equals("")){
            return ServeResponse.serveResponseByError(ResponseCode.ERROR,"图片必须上传");
        }

        //获取上传图像的名称
        String oldFileName = uploadfile.getOriginalFilename();
        //获取文件扩展名
        String extendName = oldFileName.substring(oldFileName.lastIndexOf('.'));
        //生成新的文件名
        String newFilename = UUID.randomUUID().toString()+extendName;
        File mkdir = new File("D:/picture");
        if (!mkdir.exists()){
            mkdir.mkdir();
        }
        File newFile = new File(mkdir,newFilename);
        try{
            uploadfile.transferTo(newFile);
            //http://localhost/filename
            ImageVO imageVO = new ImageVO(newFilename,imageHost + newFilename);
            return ServeResponse.serveResponseBySuccess(imageVO);

        }catch (IOException e){
            e.printStackTrace();
        }
        return ServeResponse.serveResponseByError();

    }

}
