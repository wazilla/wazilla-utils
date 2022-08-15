package de.wazilla.utils.tests.text;

import de.wazilla.utils.text.RegexVariableResolver;
import de.wazilla.utils.text.VariableResolver;

public class RegexVariableResolverTest extends AbstractVariableResolverTest {

	private VariableResolver variableResolver = new RegexVariableResolver();

	@Override
	public VariableResolver getVariableResolver() {
		return this.variableResolver;
	}

}
