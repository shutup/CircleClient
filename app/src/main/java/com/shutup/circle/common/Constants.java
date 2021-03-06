package com.shutup.circle.common;

/**
 * Created by shutup on 2016/12/14.
 */

public interface Constants {
//    String BASE_URL = "http://shutups.lan:8080";
    String BASE_URL = "https://circle.fleetinglife.cn";

    String QUESTION_ID = "QUESTION_ID";
    String ANSWER_ID = "ANSWER_ID";
    String COMMENT_ID = "COMMENT_ID";
    String USER_ID = "USER_ID";
    String HINT_STR = "HINT_STR";

    int SECTION_1 = 1;
    int SECTION_2 = 2;
    int SECTION_3 = 3;
    int SECTION_4 = 4;

    String QUESTION_ORDER_BY_TYPE = "QUESTION_ORDER_BY_TYPE";
    int QUESTION_ORDER_BY_TIME = 1;
    int QUESTION_ORDER_BY_AGREEDUSERS = 2;
    int QUESTION_ORDER_BY_DISAGREEDUSERS = 3;

    String QUESTION_ORDER_BY_AGREEUSERS = "agreeUsersCount";
    String QUESTION_ORDER_BY_DISAGREEUSERS = "disagreeUsersCount";
}
