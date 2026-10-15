package com.anseong.core.domain.client.messanger;

import com.anseong.core.domain.client.messanger.request.MessangerRequest;

public interface MessangerClient {
     void sendReportSMSNotificationToMe(MessangerRequest request);

     void sendToUserWhenPaymentFailed(MessangerRequest request);
}
