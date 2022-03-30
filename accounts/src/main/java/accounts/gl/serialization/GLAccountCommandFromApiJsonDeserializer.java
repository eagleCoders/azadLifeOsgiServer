/**
 * 
 */
package accounts.gl.serialization;

import java.lang.reflect.Type;
import java.util.Map;
import java.util.Set;

import org.apache.commons.lang3.StringUtils;

import com.google.gson.JsonElement;
import com.google.gson.reflect.TypeToken;

import accounts.gl.api.GLAccountJsonInputParams;
import accounts.gl.command.GLAccountCommand;
import infrastructure.core.exception.InvalidJsonException;
import infrastructure.core.serialization.AbstractFromApiJsonDeserializer;
import infrastructure.core.serialization.FromJsonHelper;

/**
 * @author anees-ur-rehman
 *
 */
public class GLAccountCommandFromApiJsonDeserializer extends AbstractFromApiJsonDeserializer<GLAccountCommand>  {

	 private final FromJsonHelper fromApiJsonHelper;
	 
	 public GLAccountCommandFromApiJsonDeserializer(final FromJsonHelper fromApiJsonfromApiJsonHelper) {
	        this.fromApiJsonHelper = fromApiJsonfromApiJsonHelper;
	    }
	 
	 @Override
	    public GLAccountCommand commandFromApiJson(final String json) {
	        if (StringUtils.isBlank(json)) {
	            throw new InvalidJsonException();
	        }

	        final Type typeOfMap = new TypeToken<Map<String, Object>>() {}.getType();
	        final Set<String> supportedParameters = GLAccountJsonInputParams.getAllValues();
	        this.fromApiJsonHelper.checkForUnsupportedParameters(typeOfMap, json, supportedParameters);

	        final JsonElement element = this.fromApiJsonHelper.parse(json);

	        final Long id = this.fromApiJsonHelper.extractLongNamed(GLAccountJsonInputParams.ID.getValue(), element);
	        final String name = this.fromApiJsonHelper.extractStringNamed(GLAccountJsonInputParams.NAME.getValue(), element);
	        final Long parentId = this.fromApiJsonHelper.extractLongNamed(GLAccountJsonInputParams.PARENT_ID.getValue(), element);
	        final String glCode = this.fromApiJsonHelper.extractStringNamed(GLAccountJsonInputParams.GL_CODE.getValue(), element);
	        final Boolean disabled = this.fromApiJsonHelper.extractBooleanNamed(GLAccountJsonInputParams.DISABLED.getValue(), element);
	        final Boolean manualEntriesAllowed = this.fromApiJsonHelper
	                .extractBooleanNamed(GLAccountJsonInputParams.MANUAL_ENTRIES_ALLOWED.getValue(), element);
	        final Integer type = this.fromApiJsonHelper.extractIntegerSansLocaleNamed(GLAccountJsonInputParams.TYPE.getValue(), element);
	        final Integer usage = this.fromApiJsonHelper.extractIntegerSansLocaleNamed(GLAccountJsonInputParams.USAGE.getValue(), element);
	        final String description = this.fromApiJsonHelper.extractStringNamed(GLAccountJsonInputParams.DESCRIPTION.getValue(), element);
	        final Long tagId = this.fromApiJsonHelper.extractLongNamed(GLAccountJsonInputParams.TAGID.getValue(), element);

	        return new GLAccountCommand(id, name, parentId, glCode, disabled, manualEntriesAllowed, type, usage, description, tagId);
	    }
}
