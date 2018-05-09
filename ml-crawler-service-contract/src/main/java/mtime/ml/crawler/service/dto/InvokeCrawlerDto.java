package mtime.ml.crawler.service.dto;

import lombok.Getter;
import lombok.Setter;
import mtime.lark.pb.FieldType;
import mtime.lark.pb.annotation.ProtoField;
import mtime.lark.pb.annotation.ProtoMessage;

/**
 * Dto
 * mscgenVersion: 0.4.3
 */
public class InvokeCrawlerDto {

    private InvokeCrawlerDto() {

    }

    /**
     * InvokeCrawler 响应结果
     */
    @Setter
    @Getter
    @ProtoMessage(description = "InvokeCrawler 响应结果")
    public static class InvokeCrawlerResponse {
        /**
         * 结果
         */
        @ProtoField(order = 1, type = FieldType.BOOL, required = true, description = "结果")
        private boolean result;

		@Override
		public String toString() {
			String s = getClass().getSimpleName() + ":\n";
			s +="result: " + this.result + "\n";
			return s;

		}
    }



}