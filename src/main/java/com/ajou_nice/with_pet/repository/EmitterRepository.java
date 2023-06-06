package com.ajou_nice.with_pet.repository;

import java.util.Map;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

public interface EmitterRepository {

    // Emitter 저장
    SseEmitter save(String emitterId, SseEmitter sseEmitter);
    // 이벤트 저장
    void saveEventCache(String emitterId, Object event);
    // 해당 회원과 관련된 모든 Emitter 조회
    Map<String, SseEmitter> findAllEmitterStartWithByUserId(String userId);
    // 해당 회원과 관련된 모든 이벤트 조회
    Map<String, Object> findAllEventCacheStartWithByUserId(String userId);
    // Emitter 삭제
    void deleteById(String id);
    // 해당회원과 관련된 모든 Emitter 삭제
    void deleteAllEmitterStartWithId(String userId);
    //해당 회원과 관련된 모든 이벤트 삭제
    void deleteAllEventCacheStartWithId(String userId);


}
