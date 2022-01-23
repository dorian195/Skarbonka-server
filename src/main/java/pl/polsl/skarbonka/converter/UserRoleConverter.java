package pl.polsl.skarbonka.converter;

import pl.polsl.skarbonka.model.User;

import javax.persistence.AttributeConverter;
import javax.persistence.Converter;

@Converter(autoApply = true)
public class UserRoleConverter implements AttributeConverter<User.Role, String> {
    @Override
    public String convertToDatabaseColumn(User.Role attribute) {
        if(attribute == null) {
            return null;
        }
        return attribute.getName();
    }

    @Override
    public User.Role convertToEntityAttribute(String dbData) {
        if (dbData == null) {
            return null;
        }
        return User.Role.fromName(dbData);
    }
}
