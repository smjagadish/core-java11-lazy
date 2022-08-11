package com.baeldung.data;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;

@Data
@JsonIgnoreProperties("mtls_endpoint_aliases")
public class ResponseData {
    String issuer;
    String authorization_endpoint;
    String token_endpoint;
    String introspection_endpoint;
    String userinfo_endpoint;
    String end_session_endpoint;
    Boolean frontchannel_logout_session_supported;
    Boolean frontchannel_logout_supported;
    String jwks_uri;
    String check_session_iframe;
    String[] grant_types_supported;
    int[] acr_values_supported;
    String[] response_types_supported;
    String[] subject_types_supported;
    String[] id_token_signing_alg_values_supported;
    String[] id_token_encryption_alg_values_supported;
    String[] id_token_encryption_enc_values_supported;
    String[] userinfo_signing_alg_values_supported;
    String[] userinfo_encryption_alg_values_supported;
    String[] userinfo_encryption_enc_values_supported;
    String[] request_object_signing_alg_values_supported;
    String[] request_object_encryption_alg_values_supported;
    String[] request_object_encryption_enc_values_supported;
    String[] response_modes_supported;
    String registration_endpoint;
    String[] token_endpoint_auth_methods_supported;
    String[] token_endpoint_auth_signing_alg_values_supported;
    String[] introspection_endpoint_auth_methods_supported;
    String[] introspection_endpoint_auth_signing_alg_values_supported;
    String[] authorization_signing_alg_values_supported;
    String[] authorization_encryption_alg_values_supported;
    String[] authorization_encryption_enc_values_supported;
    String[] claims_supported;
    String[] claim_types_supported;
    Boolean claims_parameter_supported;
    String[] scopes_supported;
    Boolean request_parameter_supported;
    Boolean request_uri_parameter_supported;
    Boolean require_request_uri_registration;
    String[] code_challenge_methods_supported;
    Boolean tls_client_certificate_bound_access_tokens;
    String revocation_endpoint;
    String[] revocation_endpoint_auth_methods_supported;
    String[] revocation_endpoint_auth_signing_alg_values_supported;
    Boolean backchannel_logout_supported;
    Boolean backchannel_logout_session_supported;
    String device_authorization_endpoint;
    String[] backchannel_token_delivery_modes_supported;
    String backchannel_authentication_endpoint;
    String[] backchannel_authentication_request_signing_alg_values_supported;
    Boolean require_pushed_authorization_requests;
    String pushed_authorization_request_endpoint;


}
