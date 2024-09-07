package com.ajou_nice.with_pet.pay.service;

import com.ajou_nice.with_pet.domain.entity.User;
import com.ajou_nice.with_pet.pay.model.entity.Pay;
import com.ajou_nice.with_pet.reservation.model.entity.Reservation;
import java.util.HashMap;
import java.util.Map;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;

@Service
public class KakaoPayUtilService {
    static final String cid = "TC0ONETIME";//테스트 코드
    @Value("${PAY.KEY}")
    private String secretKey;

    public HttpHeaders getHeaders() {
        //카카오 페이 서버로 요청할 header
        HttpHeaders httpHeaders = new HttpHeaders();

        String auth = "KakaoAK " + secretKey;
        httpHeaders.set("Authorization", auth);
        httpHeaders.set("Content-type", "application/x-www-form-urlencoded;charset=utf-8");
        return httpHeaders;
    }

    public Map<String, String> getReadyPayParameters(User user, Reservation reservation) {
        // 카카오페이 요청 양식
        Map<String, String> parameters = new HashMap<>();
        parameters.put("cid", cid);
        parameters.put("partner_order_id", reservation.getPetSitter().getId().toString());
        parameters.put("partner_user_id", user.getId().toString());
        parameters.put("item_name", "펫시터 예약");
        parameters.put("quantity", "1");
        parameters.put("total_amount", reservation.getReservationTotalPrice().toString());
        parameters.put("vat_amount", "0");
        parameters.put("tax_free_amount", "0");
        parameters.put("approval_url", "https://withpet.info/petsitterdetail/" + reservation.getPetSitter()
                .getId()); // 성공 시 redirect url -> 이 부분을 프론트엔드 url로 바꿔주어야 함
        parameters.put("cancel_url", "https://withpet.info/payment-cancel"); // 취소 시 redirect url -> 서버의 주소
        parameters.put("fail_url", "https://withpet.info/payment-fail"); // 실패 시 redirect url -> 서버의 주소
        // redirect url의 경우 나중에 연동시 프론트에서의 URL을 입력해주고 , 꼭 내가 도메인 변경을 해주어야 한다.

        return parameters;
    }

    public Map<String, String> getApprovePayParameters(String tid, String pgToken, Reservation reservation) {
        // 카카오 요청
        Map<String, String> parameters = new HashMap<>();
        parameters.put("cid", cid);
        parameters.put("tid", tid);
        parameters.put("partner_order_id", reservation.getPetSitter().getId().toString());
        parameters.put("partner_user_id", reservation.getUser().getId().toString());
        parameters.put("pg_token", pgToken);
        return parameters;
    }

    public Map<String, String> getAutoRefund(Pay pay) {
        // 카카오페이 요청
        Map<String, String> parameters = new HashMap<>();
        parameters.put("cid", cid);
        parameters.put("tid", pay.getTid());
        parameters.put("cancel_amount", pay.getPay_amount().toString());
        parameters.put("cancel_tax_free_amount", Integer.toString(0));
        return parameters;
    }

    public Map<String, String> getRefundParameters(Pay pay, int cancelAmount) {
        // 카카오페이 요청
        Map<String, String> parameters = new HashMap<>();
        parameters.put("cid", cid);
        parameters.put("tid", pay.getTid());
        parameters.put("cancel_amount", Integer.toString(cancelAmount));
        parameters.put("cancel_tax_free_amount", Integer.toString(0));
        return parameters;
    }
}
