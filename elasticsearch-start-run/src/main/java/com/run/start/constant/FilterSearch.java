package com.run.start.constant;

//import io.swagger.annotations.ApiModelProperty;
import javax.validation.constraints.NotEmpty;
import lombok.Builder;
import lombok.Data;
import org.elasticsearch.index.query.QueryBuilder;
import org.elasticsearch.index.query.QueryBuilders;
import org.springframework.util.StringUtils;
@Builder
@Data
public class FilterSearch {

    @NotEmpty
    //@ApiModelProperty("二次过滤字段")
    private String field;
    //@ApiModelProperty("value")
    @NotEmpty
    private String value;


    public QueryBuilder build() {
        if (StringUtils.hasText(field)){
            return QueryBuilders.termQuery(field, value);
        }
        return null;
    }


}
