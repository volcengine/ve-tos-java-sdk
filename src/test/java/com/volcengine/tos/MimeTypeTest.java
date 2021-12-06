package com.volcengine.tos;

import com.volcengine.tos.comm.MimeType;
import org.testng.annotations.Test;
import org.testng.Assert;

public class MimeTypeTest {
    @Test
    void getMimeTypeTest(){
        String xlsx = "aaa.xlsx";
        String txt = "bbb.txt";
        String notfound = "ccc.notfound";
        String defaultExt = "ddd";
        String lastIdx = "e.e.";
        try{
            Assert.assertEquals("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet", MimeType.getInstance().getMimetype(xlsx));
            Assert.assertEquals("text/plain", MimeType.getInstance().getMimetype(txt));
            Assert.assertEquals(MimeType.DEFAULT_MIMETYPE, MimeType.getInstance().getMimetype(notfound));
            Assert.assertEquals(MimeType.DEFAULT_MIMETYPE, MimeType.getInstance().getMimetype(defaultExt));
            Assert.assertEquals(MimeType.DEFAULT_MIMETYPE, MimeType.getInstance().getMimetype(lastIdx));
        } catch (TosClientException e){
            Consts.LOG.error(e.toString());
        }
    }

}
