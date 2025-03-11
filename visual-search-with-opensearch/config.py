# OpenSearch Credentials
OPENSEARCH_USERNAME='<USER_NAME>'
OPENSEARCH_PASSWORD='<PWD>'
COMPARTMENT_ID = "<YOUR_COMPARTMEN_ID>"

# OpenSearch Connection Details
OPENSEARCH_HOST = "<YOUR_OPENSEARCH_HOST>" #e.g:  .......zl7dva.opensearch.us-ashburn-1.oci.oraclecloud.com
OPENSEARCH_URL="<YOUR_OPENSEARCH_URL>"# e.g:  "https://a.................zjzl7dva.opensearch.us-ashburn-1.oci.oraclecloud.com:9200"
OPENSEARCH_PORT = 9200
AUTH_TYPE="RESOURCE_PRINCIPAL" 
OCI_GENAI_ENDPOINT = "https://inference.generativeai.us-chicago-1.oci.oraclecloud.com"
MAX_LLM_RESPONSE_PRINT=20
LLM_RESPONSE_PRINT_COUNT=0


CLUSTER_CONFIG = {
        "persistent": {
            "plugins": {
                "ml_commons": {
                    "only_run_on_ml_node": "false",
                    "model_access_control_enabled": "true",
                    "native_memory_threshold": "99",
                    "rag_pipeline_feature_enabled": "true",
                    "memory_feature_enabled": "true",
                    "allow_registering_model_via_local_file": "true",
                    "allow_registering_model_via_url": "true",
                    "model_auto_redeploy.enable": "true",
                    "model_auto_redeploy.lifetime_retry_times": 10
                }
            }
        }
    };




  
INDEX_BODY = {
    "settings": {
        "index": {
            "knn": True,
            "number_of_shards": 1,
            "number_of_replicas": 0
        }
    },
    "mappings": {
        "properties": {
            "image_id": {"type": "keyword"},
            "embedding": {
                "type": "knn_vector", "dimension": 512,
                "method": {"name": "hnsw", "engine": "lucene", "space_type": "cosinesimil"}
            }
        }
    }
}
