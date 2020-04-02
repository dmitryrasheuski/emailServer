package rash.rest.entity.email;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;

@Data
@NoArgsConstructor
@RequiredArgsConstructor
public class Email {

    private Long id;
    @NonNull
    private EmailMetaData metaData;
    @NonNull
    private EmailItem content;

}
