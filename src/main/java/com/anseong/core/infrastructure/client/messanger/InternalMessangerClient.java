package com.anseong.core.infrastructure.client.messanger;

import com.anseong.core.domain.client.messanger.MessangerClient;
import com.anseong.core.domain.client.messanger.MessangerPlatform;

public interface InternalMessangerClient extends MessangerClient {
     MessangerPlatform supports();
}
