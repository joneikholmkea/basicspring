package jon.basicspring;

import ai.onnxruntime.OnnxTensor;
import ai.onnxruntime.OnnxValue;
import ai.onnxruntime.OrtEnvironment;
import ai.onnxruntime.OrtSession;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.io.InputStream;
import java.util.Arrays;
import java.util.Collections;
import java.util.Map;

@Controller
public class BankController {

    @GetMapping("/bank")
    public String showForm(Model model) {
        return "bank";
    }

    @PostMapping("/bank")
    public String submitForm(@RequestParam("number1") double number1, @RequestParam("number2") double number2
                             ,@RequestParam("number3") double number3, @RequestParam("number4") double number4,
                             @RequestParam("number5") double number5, @RequestParam("number6") double number6,
                             @RequestParam("number7") double number7, @RequestParam("number8") double number8,
                             @RequestParam("number9") double number9, @RequestParam("number10") double number10,
                             @RequestParam("number11") double number11, @RequestParam("number12") double number12,
                             @RequestParam("number13") double number13, Model model) {
        model.addAttribute("number1", number1);
        model.addAttribute("number2", number2);
        model.addAttribute("number3", number3);
        model.addAttribute("number4", number4);
        model.addAttribute("number5", number5);
        model.addAttribute("number6", number6);
        model.addAttribute("number7", number7);
        model.addAttribute("number8", number8);
        model.addAttribute("number9", number9);
        model.addAttribute("number10", number10);
        model.addAttribute("number11", number11);
        model.addAttribute("number12", number12);
        model.addAttribute("number13", number13);
        float[] data = new float[]{(float) number1, (float) number2, (float) number3, (float) number4, (float) number5, (float) number6, (float) number7,
                (float) number8, (float) number9, (float) number10, (float) number11, (float) number12, (float) number13};
        model.addAttribute("bankpredic", runInference(data));
        return "bankresult";
    }


    public static float[] standardizeData(float[] data, float[] means, float[] stdDevs) {
        if (data.length != means.length || data.length != stdDevs.length) {
            throw new IllegalArgumentException("The length of data, means, and stdDevs must be the same.");
        }

        float[] standardizedData = new float[data.length];
        for (int i = 0; i < data.length; i++) {
            standardizedData[i] = (data[i] - means[i]) / stdDevs[i];
        }
        return standardizedData;
    }


    public float[][] reshapeTo2D(float[] originalValues, int featuresCount) {
        // Create a 2D array with 1 row and 'featuresCount' columns
        float[][] reshapedArray = new float[1][featuresCount];
        for (int i = 0; i < featuresCount; i++) {
            reshapedArray[0][i] = originalValues[i];
        }
        return reshapedArray;
    }


    String runInference(float[] data2){
        String modelPath = "/bank2.onnx";
        try (InputStream is = BankController.class.getResourceAsStream(modelPath);
             OrtEnvironment env = OrtEnvironment.getEnvironment()) {
            assert is != null;
            OrtSession session = env.createSession(is.readAllBytes());
            //float[] data = new float[]{707f, 51f, 10f, 98438f, 1f, 0f, 0f, 70778f, 0f, 1f, 0f, 1f ,0f};  // Your input data
            System.out.println("raw data " + Arrays.toString(data2));
            float[] means = new float[]{650.53f, 38.92f, 5.01f, 76485.16f, 1.53f, 0.71f, 0.52f, 100088.99f, 0.50f, 0.25f, 0.25f, 0.45f, 0.55f};
            float[] stds = new float[]{96.65f, 10.49f, 2.89f, 62397.36f, 0.58f, 0.46f, 0.50f, 57510.36f, 0.50f, 0.43f, 0.43f, 0.50f, 0.50f};
            float[] standardized = standardizeData(data2, means, stds);
            float[][] reshaped = reshapeTo2D(standardized,13);
            System.out.println("standardized and reshaped: " + Arrays.toString(reshaped[0]));
            OnnxTensor inputTensor = OnnxTensor.createTensor(env, reshaped);
            OrtSession.Result result = session.run(Collections.singletonMap("dense_4_input", inputTensor));

            float[][] output = (float[][]) result.get(0).getValue();
            System.out.println(Arrays.toString(output[0]));
            return Arrays.toString(output[0]);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

}
