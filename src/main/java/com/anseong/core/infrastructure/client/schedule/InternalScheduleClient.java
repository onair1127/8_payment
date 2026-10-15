package com.anseong.core.infrastructure.client.schedule;

import com.anseong.core.domain.client.schedule.ScheduleClient;
import com.anseong.core.domain.client.schedule.SchedulePlatform;

public interface InternalScheduleClient extends ScheduleClient {
     SchedulePlatform supports();
}
