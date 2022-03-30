/**
 * 
 */
package accounts.gl.command;

import java.util.ArrayList;
import java.util.List;

import accounts.gl.api.GLAccountJsonInputParams;
import accounts.gl.domain.GLAccountType;
import accounts.gl.domain.GLAccountUsage;
import infrastructure.core.data.ApiParameterError;
import infrastructure.core.data.DataValidatorBuilder;
import infrastructure.core.exception.PlatformApiDataValidationException;

/**
 * @author anees-ur-rehman
 *
 */
public class GLAccountCommand {

	@SuppressWarnings("unused")
    private final Long id;
    private final String name;
    private final Long parentId;
    private final String glCode;
    private final Boolean disabled;
    private final Boolean manualEntriesAllowed;
    private final Integer usage;
    private final Integer type;
    private final String description;
    private final Long tagId;
    
    public GLAccountCommand(final Long id, final String name, final Long parentId, final String glCode, final Boolean disabled,
            final Boolean manualEntriesAllowed, final Integer type, final Integer usage, final String description, final Long tagId) {
        this.id = id;
        this.name = name;
        this.parentId = parentId;
        this.glCode = glCode;
        this.disabled = disabled;
        this.manualEntriesAllowed = manualEntriesAllowed;
        this.type = type;
        this.usage = usage;
        this.description = description;
        this.tagId = tagId;
    }
    
    public void validateForCreate() {

        final List<ApiParameterError> dataValidationErrors = new ArrayList<>();

        final DataValidatorBuilder baseDataValidator = new DataValidatorBuilder(dataValidationErrors).resource("GLAccount");

        baseDataValidator.reset().parameter(GLAccountJsonInputParams.NAME.getValue()).value(this.name).notBlank().notExceedingLengthOf(200);

        baseDataValidator.reset().parameter(GLAccountJsonInputParams.GL_CODE.getValue()).value(this.glCode).notBlank()
                .notExceedingLengthOf(45);

        baseDataValidator.reset().parameter(GLAccountJsonInputParams.PARENT_ID.getValue()).value(this.parentId).ignoreIfNull()
                .integerGreaterThanZero();

        baseDataValidator.reset().parameter(GLAccountJsonInputParams.TYPE.getValue()).value(this.type).notNull()
                .inMinMaxRange(GLAccountType.getMinValue(), GLAccountType.getMaxValue());

        baseDataValidator.reset().parameter(GLAccountJsonInputParams.USAGE.getValue()).value(this.usage)
                .inMinMaxRange(GLAccountUsage.getMinValue(), GLAccountUsage.getMaxValue());

        baseDataValidator.reset().parameter(GLAccountJsonInputParams.DESCRIPTION.getValue()).value(this.description).ignoreIfNull()
                .notExceedingLengthOf(500);

        baseDataValidator.reset().parameter(GLAccountJsonInputParams.MANUAL_ENTRIES_ALLOWED.getValue()).value(this.manualEntriesAllowed)
                .notBlank();

        baseDataValidator.reset().parameter(GLAccountJsonInputParams.TAGID.getValue()).value(this.tagId).ignoreIfNull()
                .longGreaterThanZero();

        if (!dataValidationErrors.isEmpty()) {
            throw new PlatformApiDataValidationException("validation.msg.validation.errors.exist", "Validation errors exist.",
                    dataValidationErrors);
        }
    }

    
    public void validateForUpdate() {
        final List<ApiParameterError> dataValidationErrors = new ArrayList<>();

        final DataValidatorBuilder baseDataValidator = new DataValidatorBuilder(dataValidationErrors).resource("GLAccount");

        baseDataValidator.reset().parameter(GLAccountJsonInputParams.NAME.getValue()).value(this.name).ignoreIfNull().notBlank()
                .notExceedingLengthOf(200);

        baseDataValidator.reset().parameter(GLAccountJsonInputParams.GL_CODE.getValue()).ignoreIfNull().value(this.glCode).notBlank()
                .notExceedingLengthOf(45);

        baseDataValidator.reset().parameter(GLAccountJsonInputParams.PARENT_ID.getValue()).value(this.parentId).ignoreIfNull()
                .integerGreaterThanZero();

        baseDataValidator.reset().parameter(GLAccountJsonInputParams.TYPE.getValue()).value(this.type).ignoreIfNull()
                .inMinMaxRange(GLAccountType.getMinValue(), GLAccountType.getMaxValue());
        baseDataValidator.reset().parameter(GLAccountJsonInputParams.USAGE.getValue()).value(this.usage).ignoreIfNull()
                .inMinMaxRange(GLAccountUsage.getMinValue(), GLAccountUsage.getMaxValue());

        baseDataValidator.reset().parameter(GLAccountJsonInputParams.DESCRIPTION.getValue()).value(this.description).ignoreIfNull()
                .notBlank().notExceedingLengthOf(500);

        baseDataValidator.reset().parameter(GLAccountJsonInputParams.DISABLED.getValue()).value(this.disabled).ignoreIfNull();

        baseDataValidator.reset().anyOfNotNull(this.name, this.glCode, this.parentId, this.type, this.description, this.disabled);

        if (!dataValidationErrors.isEmpty()) {
            throw new PlatformApiDataValidationException("validation.msg.validation.errors.exist", "Validation errors exist.",
                    dataValidationErrors);
        }
        baseDataValidator.reset().parameter(GLAccountJsonInputParams.TAGID.getValue()).value(this.tagId).ignoreIfNull()
                .longGreaterThanZero();
    }
    
    public boolean isHeaderAccount() {
        return GLAccountUsage.HEADER.getValue().equals(this.usage);
    }

}
