package tvgirl.elmodev.e1m0admin.api;

public class E1m0ProviderAPI {
    private static E1m0AdminAPI api;

    public static void register(E1m0AdminAPI plugin) {
        api = plugin;
    }

    public static E1m0AdminAPI get() {
        if (api == null) {
            throw new IllegalStateException("API - НЕ БЫЛО ЗАРЕГИСТРИРОВАНО!");
        }
        return api;
    }
}
