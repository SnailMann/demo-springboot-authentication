package com.snailmann.security.demo.wiremock;
import com.google.common.base.Charsets;
import org.apache.commons.io.FileUtils;
import org.springframework.core.io.ClassPathResource;
import java.io.IOException;
import static com.github.tomakehurst.wiremock.client.WireMock.*;

/**
 * 这是一个Mock客户端，用于连接我们所启动的WireMock服务端
 * 将我们需要的匹配规则加入到wiremock
 */
public class MockClient {

    public static void main(String[] args) throws IOException {
        configureFor(8062);
        removeAllMappings(); //把以前的所有配置都清空，因为我们可能会经常更新，所以我们每次启动都需要清理以前的配置、
        mock("/order/1","01");
        mock("/order/2","02");

    }

    /**
     *
     * @param url 要匹配的接口url
     * @param fileName 返回的数据放到文件中去读取
     * @throws IOException
     */
    private static void mock(String url,String fileName) throws IOException {
        ClassPathResource  resource = new ClassPathResource("\\mock\\response\\"+fileName+".txt");
        String content = FileUtils.readFileToString(resource.getFile(), Charsets.UTF_8);
        stubFor(get(urlPathEqualTo(url))          //get方法，匹配正则表达式
                .willReturn(aResponse()
                        .withBody(content)
                        .withStatus(200)));
    }
}
