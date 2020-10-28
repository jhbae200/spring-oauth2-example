package com.example.oauth2example.data.dao;

import com.example.oauth2example.dao.mysql.OAuthJwtRsaKey;

import java.io.StringReader;
import java.util.Date;

public class TestOAuthJwtRasKey {
    /*
    publickey, privatekey by jwt.io rs256 default key.
     */
    public static final String name = "testRsaKey";
    public static final String privateKey = "-----BEGIN PRIVATE KEY-----\n" +
            "MIIEvQIBADANBgkqhkiG9w0BAQEFAASCBKcwggSjAgEAAoIBAQDCkPxxu4WlwD4c\n" +
            "ZeHFL/zwRyDK380lPaeDZxIUgPacrUW1MHKbw/fY73tiPPXwj56u8AHtWfHgdLZO\n" +
            "uhuAwSxIGdF1Ix22vjpzicFL9BYGF62Z0acADMtfkJsYn76juHFEPqEWU82GEpsP\n" +
            "JIO/WpoO+sFwN9CKqBZKOh+iTBpn4h0krARtuz+/mV4dn/EJ7aro6u951/dd1c7m\n" +
            "VMj5jQbsbsMyHHvqFboQwCpjlgDT2+E/XIaC5wJBgAnOXVZbvm9eGXCBFVFizJbw\n" +
            "Scokgoo5Xrn7Mg4oF6upDv2gZaoCJ2hf9qchANaGa2qjhRwD4XvAyceWliaBbKj6\n" +
            "crPOoyRpAgMBAAECggEAAiiHlF9nKSKLzdbXfU9999BiLb3z0aDwdcWz7qRLmAtY\n" +
            "BZ2G9/Fhk3d8n+dbNxLwyLIC43Ym971ztEXePjxmWFpo92qvTrKnhVaW3VfU0rZ6\n" +
            "h4VK2/ZIX0QEZG6JN9m47+/d65h8ZCtaqv1VtM61fMsNUEheONuKe8JPTYaYmGRl\n" +
            "vlZ5BghP/7kjcOJcxxdi/slqOo3dMnHUjccgv/zVeCNLz3H3a/wZawAg/nyX8xpm\n" +
            "lWwvPSwnexUh5E3RJJJv3SbXwpcwfuf+E1P3PFAz9IkAhwxyWyP8luKFDGmW9ff8\n" +
            "tsEP2UI9xu7mk4XqGCe/AkEM2YPpmzndxosvx5CCHQKBgQD9t8LXda85hvf5M+sj\n" +
            "iYSbmRNk7BKEs8hiif7mtPEGQDUcH4TVkSx+PEsubiHAZWwYClcf/ydBt9ivXesu\n" +
            "TLt5I69QFWyJH/Y8djIf3u3xKw16J2kH80Dz/EVdzq2yTu8hxqc2zgzjTAuMxKT3\n" +
            "rFGAUS2/j8s819ueSlF4S+B9MwKBgQDEUQQpnzD8s3nlyQb38yXFp7sDWJERXft3\n" +
            "EAju53DhsfEhtKbWm0fQvyLweUv0ekkpZsDal66eplAIl0hykttemqZfTLB/sC2w\n" +
            "SMeIXm1qqq3WhZ76YRYyQxwL8pgk7bErOUHYcTRgrfy8+RzX56P+9Qv5CbGEMxVx\n" +
            "Xb6Fi9B/8wKBgQCxFYdKtvscHdYJ+3DNFviVWZwtvo+WS507GbHt+LCZeksXW/Bs\n" +
            "16zEVvTqbTflf2SYyu7QiFgwCE+W5uh1Il1fScbdEHb2pnZI8P4/PGu6/h+j+MnY\n" +
            "ILJfGN6ZgN2/LFS/sFWZbOuV4cfV8pJpw74G36IcqBJkXw0MSDbgzaTb/QKBgCOk\n" +
            "vge8ko1fHMO+wxgjunjvokHSQ8yObsjITYbq2JDx2OEQxrXyTZC2E2br7wF9bmko\n" +
            "8dz+a0L82U0mAo5i4ZeEpq5o4ybb2v/FxwyYcG9Me/GWIy48kGYhzybQLhWOXnuF\n" +
            "ktH0g9kF3fyFkloVAa6Z2UmnG+bvy3Xg2mWu4/VHAoGANcLPzDA36Q2hwKoqePfj\n" +
            "E30blnnPLoe7N1nffHrf/PWAtfoc8JUd9c04J4mBid/K2iUtHId9zwQdKxktm8zI\n" +
            "5Ig2ykThuO9VUAyt4UiJr95zlkk5BQy8HYrOE2H8F5CKggYNoyeYQ60MaJ0C1feG\n" +
            "ptRL2Pq68CsuJcOry3V5tCU=\n" +
            "-----END PRIVATE KEY-----";
    public static final String publicKey = "-----BEGIN PUBLIC KEY-----\n" +
            "MIIBIjANBgkqhkiG9w0BAQEFAAOCAQ8AMIIBCgKCAQEAwpD8cbuFpcA+HGXhxS/8\n" +
            "8Ecgyt/NJT2ng2cSFID2nK1FtTBym8P32O97Yjz18I+ervAB7Vnx4HS2TrobgMEs\n" +
            "SBnRdSMdtr46c4nBS/QWBhetmdGnAAzLX5CbGJ++o7hxRD6hFlPNhhKbDySDv1qa\n" +
            "DvrBcDfQiqgWSjofokwaZ+IdJKwEbbs/v5leHZ/xCe2q6Orvedf3XdXO5lTI+Y0G\n" +
            "7G7DMhx76hW6EMAqY5YA09vhP1yGgucCQYAJzl1WW75vXhlwgRVRYsyW8EnKJIKK\n" +
            "OV65+zIOKBerqQ79oGWqAidoX/anIQDWhmtqo4UcA+F7wMnHlpYmgWyo+nKzzqMk\n" +
            "aQIDAQAB\n" +
            "-----END PUBLIC KEY-----";
    public static final Date createdDate = new Date(1603359529827L); //2020-10-22T09:38:49.827Z

    public static final String name2 = "testRsaKey2";
    public static final String privateKey2 = "-----BEGIN PRIVATE KEY-----\n" +
            "MIIEvgIBADANBgkqhkiG9w0BAQEFAASCBKgwggSkAgEAAoIBAQDTNuoumrl9yEkj\n" +
            "HFMLk0V40lIHeEUes/QzLx7EKrSoEyJJTRYDhe0USqgPuyO4QRKmUxN5RgitAway\n" +
            "VVBExVIBa7q0MPMTjwedWPF7EsVoZONplRXMqoZMaqd5kgl29hhmHAkX1zHJhYXX\n" +
            "OAyGzjfhamRCsrAgIlfNUcnTXt8WAJP81cNlGXzScRbeQY7xi/yQnMmNjV8ayn5d\n" +
            "pOqgQDWSruVzYXw+fxQrEI/4pnSll9hE8CwenTWXwE03pHxdpFzJaPUECvJHblMj\n" +
            "JX8BcwyWOcM+vFLv10z8lIkWC5jSvGtHDnbB0+LARA3jM2z9abnux4rHciThg9lN\n" +
            "FiaP/lo7AgMBAAECggEAKRtv+oAMh+l6KoYxt3gDNl+grWl/8ljNt0R9dWpmXKvU\n" +
            "qXWCSk8/dAqPqPggHJU1nv9eM+UofRuwqvalt6y7qCEiZj18oZrdvYb5AJ4Ho6i3\n" +
            "Be6JHps1phu/2o5Mn0WIPB9KucQdIX6GLPvwnC/4ejutpbmHQ8nLUt+PKz80peA+\n" +
            "MgPu49T7P0SBZxhVSTN/LwwHiyaEGAtrebznHxWpPYE9hOhF6Zef7H5xVl3Q5zgl\n" +
            "ArAD9wmjlgZ1ijVr8+qL0xAEdypOrAaPvI2Mb5dN9FeZ2/rSM9KFI7/5HZzAdLvx\n" +
            "HujjE4iGspE36XIhr9IvIvBqZKKBW3jih6UE01MI6QKBgQD5OWT8BPP/F0n33hZo\n" +
            "DXz2fLJDDhcELFME1zoymiPTzG/T5Rs4y5TIM1wQe4LZPRzpMrZSASZuUQZX854/\n" +
            "JJ81rhW79P8uFm1rmXFm3OfDLdjIZF0bCRBRCERB/ETqtDXTY6NKM94+Pxq7Q4o7\n" +
            "8Rx2f6KwiGs2s/dJXF9Dpe6QxQKBgQDY9Pjb7rr0e8uGkqD1XbXxX8dPTNz9u460\n" +
            "uuTPlkMSSqmK2/jGT8fs84DCbY+ufpjVmuLK+xdPcRMKOd6TlF8N02AT2CMxJdbx\n" +
            "EbIlztkW4IzZn+SLHwVDj4l7Rbu4YrewVlevkRQVwsYVk2Wcgby8dQibSKm5DTXl\n" +
            "xX3Q/AHu/wKBgQDpkWye9FVnwH/GGRGOuvVeXEcEqPPRKWk0pDEas3LYUESNpa35\n" +
            "cHIjnl6t49MhnTC3LJvKBuHpiW4Uh3DLZJsBSAF+gYGVw9cQfGHXMTjTpugABm9r\n" +
            "ovbzuneeD7rW68V2M28ZX7rtQ17eGdI3kyRFuiDSqUcxoUAkNLnjQGNwsQKBgCWi\n" +
            "8ZztoB8Tbq4st6F5HTv/OosmMVwO8mWl2lWsonwE2b6OBG0an/saDucrrHY/3fGI\n" +
            "Sfmeb1/HpN7sRRFi9s5mMc+fehNnlg8B2961MHHIiIakNzhvLDGZL+djpHoLHgSI\n" +
            "JfP9/fb8b/KEPGdXFk+3GcQeadsBUK1IglgDr64vAoGBAJ/ICOQf3cSvHqSTJ9uA\n" +
            "ZUba8YRbiKKYIKW2Aj+BiFHoKaaiuO1x+Qw42I/xizMDleePOgHqs8SIJcpBZWNj\n" +
            "zM/UwqBQX2RgRj0vGh7HNBak0NpJxZQczvvVsGqlGJjwUCToSlfeB+Z5meQeSX0h\n" +
            "ZZQtpYfQAeDc8IvY5MNHXgTZ\n" +
            "-----END PRIVATE KEY-----";
    public static final String publicKey2 = "-----BEGIN PUBLIC KEY-----\n" +
            "MIIBIjANBgkqhkiG9w0BAQEFAAOCAQ8AMIIBCgKCAQEA0zbqLpq5fchJIxxTC5NF\n" +
            "eNJSB3hFHrP0My8exCq0qBMiSU0WA4XtFEqoD7sjuEESplMTeUYIrQMGslVQRMVS\n" +
            "AWu6tDDzE48HnVjxexLFaGTjaZUVzKqGTGqneZIJdvYYZhwJF9cxyYWF1zgMhs43\n" +
            "4WpkQrKwICJXzVHJ017fFgCT/NXDZRl80nEW3kGO8Yv8kJzJjY1fGsp+XaTqoEA1\n" +
            "kq7lc2F8Pn8UKxCP+KZ0pZfYRPAsHp01l8BNN6R8XaRcyWj1BAryR25TIyV/AXMM\n" +
            "ljnDPrxS79dM/JSJFguY0rxrRw52wdPiwEQN4zNs/Wm57seKx3Ik4YPZTRYmj/5a\n" +
            "OwIDAQAB\n" +
            "-----END PUBLIC KEY-----";
    public static final Date createdDate2 = new Date(1603359529827L); //2020-10-22T09:38:49.827Z


    public static OAuthJwtRsaKey getTestData() {
        OAuthJwtRsaKey oAuthJwtRsaKey = new OAuthJwtRsaKey();
        oAuthJwtRsaKey.setName(name);
        oAuthJwtRsaKey.setPrivateKey(new StringReader(privateKey));
        oAuthJwtRsaKey.setPublicKey(new StringReader(publicKey));
        return oAuthJwtRsaKey;
    }

    public static OAuthJwtRsaKey getTestData2() {
        OAuthJwtRsaKey oAuthJwtRsaKey = new OAuthJwtRsaKey();
        oAuthJwtRsaKey.setName(name2);
        oAuthJwtRsaKey.setPrivateKey(new StringReader(privateKey2));
        oAuthJwtRsaKey.setPublicKey(new StringReader(publicKey2));
        return oAuthJwtRsaKey;
    }
}
