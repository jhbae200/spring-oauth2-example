package com.example.oauth2example.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Data;

import java.util.List;

public class WellKnownDTO {
    @Data
    @JsonInclude(JsonInclude.Include.NON_NULL)
    public static class OpenIdConfiguration {
        private String issuer;
        private String authorization_endpoint;
        private String token_endpoint;
        private String userinfo_endpoint;
        private String revocation_endpoint;
        private String jwks_uri;
        private String registration_endpoint;
        private List<String> scopes_supported;
        private List<String> response_types_supported;
        private List<String> response_modes_supported;
        private List<String> grant_types_supported;
        private List<String> acr_values_supported;
        private List<String> subject_types_supported;
        private List<String> id_token_signing_alg_values_supported;
        private List<String> id_token_encryption_alg_values_supported;
        private List<String> id_token_encryption_enc_values_supported;
        private List<String> userinfo_signing_alg_values_supported;
        private List<String> userinfo_encryption_alg_values_supported;
        private List<String> userinfo_encryption_enc_values_supported;
        private List<String> request_object_signing_alg_values_supported;
        private List<String> request_object_encryption_alg_values_supported;
        private List<String> request_object_encryption_enc_values_supported;
        private List<String> token_endpoint_auth_methods_supported;
        private List<String> token_endpoint_auth_signing_alg_values_supported;
        private List<String> display_values_supported;
        private List<String> claim_types_supported;
        private List<String> claims_supported;
        private List<String> code_challenge_methods_supported;
        private List<String> service_documentation;
        private Boolean claims_locales_supported;
        private Boolean ui_locales_supported;
        private Boolean claims_parameter_supported;
        private Boolean request_parameter_supported;
        private Boolean request_uri_parameter_supported;
        private Boolean require_request_uri_registration;
        private String op_policy_uri;
        private String op_tos_uri;
        private String check_session_iframe;
        private String end_session_endpoint;
        private String frontchannel_logout_supported;
        private String frontchannel_logout_session_supported;
    }
}
