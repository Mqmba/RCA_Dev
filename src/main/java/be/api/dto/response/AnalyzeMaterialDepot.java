package be.api.dto.response;

import lombok.Data;

import java.io.Serializable;
import java.util.List;

@Data
public class AnalyzeMaterialDepot  implements Serializable {
    public List<AnalyzeItem> dataAnalyze;

    @Data
    public static class AnalyzeItem {
        private String MaterialTypeName;
        private double totalWeight;

        public AnalyzeItem(String MaterialTypeName, double totalWeight) {
            this.MaterialTypeName = MaterialTypeName;
            this.totalWeight = totalWeight;
        }
    }
}
