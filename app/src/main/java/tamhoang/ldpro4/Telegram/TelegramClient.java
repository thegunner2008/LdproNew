package tamhoang.ldpro4.Telegram;

import org.drinkless.td.libcore.telegram.Client;
import org.drinkless.td.libcore.telegram.TdApi;
import tamhoang.ldpro4.Util;

public class TelegramClient {
    private static Client client;

    public interface Callback extends Client.ResultHandler {
        void onResult(TdApi.Object object);
    }

    private TelegramClient() {
    }

    public static Client getClient(Callback callback) {
        if (client == null) {
            try {
                client = Client.create(callback,  null, null);
            } catch (Exception e) {
                Util.writeLog(e);
            }
        }
        return client;
    }
}
