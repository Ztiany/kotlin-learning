package explore.retrofit;

import io.reactivex.annotations.NonNull;
import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import retrofit2.Converter;
import retrofit2.Retrofit;

import java.lang.annotation.Annotation;
import java.lang.reflect.Type;

/**
 * json 解析容错处理，<a href="http://blog.piasy.com/2016/09/04/RESTful-Android-Network-Solution-2/">参考</a>
 *
 * @author Ztiany
 */
public class ErrorJsonLenientConverterFactory extends Converter.Factory {

    private final Converter.Factory mGsonConverterFactory;

    public ErrorJsonLenientConverterFactory(Converter.Factory gsonConverterFactory) {
        mGsonConverterFactory = gsonConverterFactory;
    }

    @Override
    public Converter<?, RequestBody> requestBodyConverter(@NonNull Type type, @NonNull Annotation[] parameterAnnotations, @NonNull Annotation[] methodAnnotations, @NonNull Retrofit retrofit) {

        System.out.printf("requestBodyConverter --> type is %s \n", type);

        return mGsonConverterFactory.requestBodyConverter(type, parameterAnnotations, methodAnnotations, retrofit);
    }

    @Override
    public Converter<ResponseBody, ?> responseBodyConverter(@NonNull Type type, @NonNull Annotation[] annotations, @NonNull Retrofit retrofit) {

        System.out.printf("responseBodyConverter --> type is %s \n", type);

        final Converter<ResponseBody, ?> delegateConverter = mGsonConverterFactory.responseBodyConverter(type, annotations, retrofit);
        assert delegateConverter != null;

        return (Converter<ResponseBody, Object>) value -> {
            try {
                return delegateConverter.convert(value);
            } catch (Exception e/*JsonSyntaxException、IOException or MalformedJsonException*/) {
                throw new ServerErrorException(ServerErrorException.SERVER_DATA_ERROR);
            }
        };
    }

}