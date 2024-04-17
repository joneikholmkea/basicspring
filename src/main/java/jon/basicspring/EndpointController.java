package jon.basicspring;

import ai.onnxruntime.OnnxTensor;
import ai.onnxruntime.OrtEnvironment;
import ai.onnxruntime.OrtSession;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.InputStream;
import java.util.Arrays;
import java.util.Collections;

@RestController
public class EndpointController {

    @GetMapping("/home")
    public ResponseEntity<String> home()
    {
        runInference();
        return ResponseEntity.ok("you are home");
    }

    void runInference(){
        String modelPath = "/xorPrev.onnx";
        try (InputStream is = EndpointController.class.getResourceAsStream(modelPath);
                OrtEnvironment env = OrtEnvironment.getEnvironment()) {
            assert is != null;
            OrtSession session = env.createSession(is.readAllBytes());
            float[][] inputData = new float[][]{{0.0f, 1.0f}};  // Your input data
            OnnxTensor inputTensor = OnnxTensor.createTensor(env, inputData);
            OrtSession.Result result = session.run(Collections.singletonMap("dense_input", inputTensor));

            float[][] output = (float[][]) result.get(0).getValue();
            System.out.println("Model output: " + Arrays.toString(output[0]));
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

}
