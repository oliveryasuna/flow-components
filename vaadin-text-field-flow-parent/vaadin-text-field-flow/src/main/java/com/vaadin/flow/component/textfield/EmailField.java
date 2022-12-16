/*
 * Copyright 2000-2022 Vaadin Ltd.
 *
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not
 * use this file except in compliance with the License. You may obtain a copy of
 * the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS, WITHOUT
 * WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the
 * License for the specific language governing permissions and limitations under
 * the License.
 */

package com.vaadin.flow.component.textfield;

import com.vaadin.flow.component.AttachEvent;
import com.vaadin.flow.component.CompositionNotifier;
import com.vaadin.flow.component.HasHelper;
import com.vaadin.flow.component.HasLabel;
import com.vaadin.flow.component.HasSize;
import com.vaadin.flow.component.HasStyle;
import com.vaadin.flow.component.HasValidation;
import com.vaadin.flow.component.InputNotifier;
import com.vaadin.flow.component.KeyNotifier;
import com.vaadin.flow.component.Tag;
import com.vaadin.flow.component.dependency.JsModule;
import com.vaadin.flow.component.dependency.NpmPackage;
import com.vaadin.flow.component.shared.ClientValidationUtil;
import com.vaadin.flow.component.shared.HasAllowedCharPattern;
import com.vaadin.flow.component.shared.HasClearButton;
import com.vaadin.flow.component.shared.HasClientValidation;
import com.vaadin.flow.component.shared.HasThemeVariant;
import com.vaadin.flow.component.shared.HasTooltip;
import com.vaadin.flow.data.binder.Binder;
import com.vaadin.flow.data.binder.HasValidator;
import com.vaadin.flow.data.binder.ValidationStatusChangeEvent;
import com.vaadin.flow.data.binder.ValidationStatusChangeListener;
import com.vaadin.flow.data.binder.Validator;
import com.vaadin.flow.data.value.HasValueChangeMode;
import com.vaadin.flow.data.value.ValueChangeMode;
import com.vaadin.flow.shared.Registration;

/**
 * Email Field is an extension of Text Field that only accepts email addresses
 * as input. If the given address is invalid, the field is highlighted in red
 * and an error message appears underneath the input. The validity of the email
 * addresses is checked according to the RFC 5322 standard, which includes the
 * format for email addresses. The component also supports supplying additional
 * validation criteria using a regular expression (see
 * {@link #setPattern(String)}). These extra validation criteria can be used,
 * for example, to require a specific email domain.
 *
 * @author Vaadin Ltd.
 */
@Tag("vaadin-email-field")
@NpmPackage(value = "@vaadin/polymer-legacy-adapter", version = "24.0.0-alpha6")
@JsModule("@vaadin/polymer-legacy-adapter/style-modules.js")
@NpmPackage(value = "@vaadin/email-field", version = "24.0.0-alpha6")
@JsModule("@vaadin/email-field/src/vaadin-email-field.js")
public class EmailField extends GeneratedVaadinTextField<EmailField, String>
        implements CompositionNotifier, HasAllowedCharPattern,
        HasAutocapitalize, HasAutocomplete, HasAutocorrect, HasClearButton,
        HasClientValidation, HasHelper, HasLabel, HasPrefixAndSuffix, HasSize,
        HasStyle, HasThemeVariant<TextFieldVariant>, HasTooltip, HasValidation,
        HasValidator<String>, HasValueChangeMode, InputNotifier, KeyNotifier {
    private static final String EMAIL_PATTERN = "^" + "([a-zA-Z0-9_\\.\\-+])+" // local
            + "@" + "[a-zA-Z0-9-.]+" // domain
            + "\\." + "[a-zA-Z0-9-]{2,}" // tld
            + "$";

    private ValueChangeMode currentMode;

    private boolean isConnectorAttached;

    private int valueChangeTimeout = DEFAULT_CHANGE_TIMEOUT;

    private TextFieldValidationSupport validationSupport;

    /**
     * Constructs an empty {@code EmailField}.
     */
    public EmailField() {
        super("", "", false, true);

        // workaround for https://github.com/vaadin/flow/issues/3496
        setInvalid(false);

        setValueChangeMode(ValueChangeMode.ON_CHANGE);

        addValueChangeListener(e -> validate());

        addClientValidatedEventListener(e -> validate());
    }

    /**
     * Constructs an empty {@code EmailField} with the given label.
     *
     * @param label
     *            the text to set as the label
     */
    public EmailField(String label) {
        this();
        setLabel(label);
    }

    /**
     * Constructs an empty {@code EmailField} with the given label and
     * placeholder text.
     *
     * @param label
     *            the text to set as the label
     * @param placeholder
     *            the placeholder text to set
     */
    public EmailField(String label, String placeholder) {
        this(label);
        setPlaceholder(placeholder);
    }

    /**
     * Constructs an empty {@code EmailField} with a value change listener.
     *
     * @param listener
     *            the value change listener
     *
     * @see #addValueChangeListener(com.vaadin.flow.component.HasValue.ValueChangeListener)
     */
    public EmailField(
            ValueChangeListener<? super ComponentValueChangeEvent<EmailField, String>> listener) {
        this();
        addValueChangeListener(listener);
    }

    /**
     * Constructs an empty {@code EmailField} with a value change listener and a
     * label.
     *
     * @param label
     *            the text to set as the label
     * @param listener
     *            the value change listener
     *
     * @see #setLabel(String)
     * @see #addValueChangeListener(com.vaadin.flow.component.HasValue.ValueChangeListener)
     */
    public EmailField(String label,
            ValueChangeListener<? super ComponentValueChangeEvent<EmailField, String>> listener) {
        this(label);
        addValueChangeListener(listener);
    }

    /**
     * Constructs a {@code EmailField} with a value change listener, a label and
     * an initial value.
     *
     * @param label
     *            the text to set as the label
     * @param initialValue
     *            the initial value
     * @param listener
     *            the value change listener
     *
     * @see #setLabel(String)
     * @see #setValue(Object)
     * @see #addValueChangeListener(com.vaadin.flow.component.HasValue.ValueChangeListener)
     */
    public EmailField(String label, String initialValue,
            ValueChangeListener<? super ComponentValueChangeEvent<EmailField, String>> listener) {
        this(label);
        setValue(initialValue);
        addValueChangeListener(listener);
    }

    private TextFieldValidationSupport getValidationSupport() {
        if (validationSupport == null) {
            validationSupport = new TextFieldValidationSupport(this);
            validationSupport.setPattern(EMAIL_PATTERN);
        }
        return validationSupport;
    }

    /**
     * {@inheritDoc}
     * <p>
     * The default value is {@link ValueChangeMode#ON_CHANGE}.
     */
    @Override
    public ValueChangeMode getValueChangeMode() {
        return currentMode;
    }

    @Override
    public void setValueChangeMode(ValueChangeMode valueChangeMode) {
        currentMode = valueChangeMode;
        setSynchronizedEvent(
                ValueChangeMode.eventForMode(valueChangeMode, "value-changed"));
        applyChangeTimeout();
    }

    @Override
    public void setValueChangeTimeout(int valueChangeTimeout) {
        this.valueChangeTimeout = valueChangeTimeout;
        applyChangeTimeout();
    }

    @Override
    public int getValueChangeTimeout() {
        return valueChangeTimeout;
    }

    private void applyChangeTimeout() {
        ValueChangeMode.applyChangeTimeout(getValueChangeMode(),
                getValueChangeTimeout(), getSynchronizationRegistration());
    }

    @Override
    public String getErrorMessage() {
        return super.getErrorMessageString();
    }

    @Override
    public void setErrorMessage(String errorMessage) {
        super.setErrorMessage(errorMessage);
    }

    @Override
    public boolean isInvalid() {
        return isInvalidBoolean();
    }

    @Override
    public void setInvalid(boolean invalid) {
        super.setInvalid(invalid);
    }

    @Override
    public void setLabel(String label) {
        super.setLabel(label);
    }

    /**
     * String used for the label element.
     *
     * @return the {@code label} property from the webcomponent
     */
    @Override
    public String getLabel() {
        return getLabelString();
    }

    @Override
    public void setPlaceholder(String placeholder) {
        super.setPlaceholder(placeholder);
    }

    /**
     * A hint to the user of what can be entered in the component.
     *
     * @return the {@code placeholder} property from the webcomponent
     */
    public String getPlaceholder() {
        return getPlaceholderString();
    }

    @Override
    public void setAutofocus(boolean autofocus) {
        super.setAutofocus(autofocus);
    }

    /**
     * Specify that this control should have input focus when the page loads.
     *
     * @return the {@code autofocus} property from the webcomponent
     */
    public boolean isAutofocus() {
        return isAutofocusBoolean();
    }

    /**
     * Maximum number of characters (in Unicode code points) that the user can
     * enter.
     *
     * @param maxLength
     *            the maximum length
     */
    public void setMaxLength(int maxLength) {
        getElement().setProperty("maxlength", maxLength);
        getValidationSupport().setMaxLength(maxLength);
    }

    /**
     * Maximum number of characters (in Unicode code points) that the user can
     * enter.
     *
     * @return the {@code maxlength} property from the webcomponent
     */
    public int getMaxLength() {
        return (int) getElement().getProperty("maxlength", 0.0);
    }

    /**
     * Minimum number of characters (in Unicode code points) that the user can
     * enter.
     *
     * @param minLength
     *            the minimum length
     */
    public void setMinLength(int minLength) {
        getElement().setProperty("minlength", minLength);
        getValidationSupport().setMinLength(minLength);
    }

    /**
     * Minimum number of characters (in Unicode code points) that the user can
     * enter.
     *
     * @return the {@code minlength} property from the webcomponent
     */
    public int getMinLength() {
        return (int) getElement().getProperty("minlength", 0.0);
    }

    /**
     * Sets a regular expression for the value to pass on the client-side. The
     * pattern must be a valid JavaScript Regular Expression that matches the
     * entire value, not just some subset.
     *
     * @param pattern
     *            the new String pattern
     *
     * @see <a href=
     *      "https://developer.mozilla.org/en-US/docs/Web/HTML/Element/input#htmlattrdefpattern">
     *      https://developer.mozilla.org/en-US/docs/Web/HTML/Element/input#htmlattrdefpattern</>
     * @see <a href=
     *      "https://html.spec.whatwg.org/multipage/input.html#attr-input-pattern">
     *      https://html.spec.whatwg.org/multipage/input.html#attr-input-pattern</>
     */
    public void setPattern(String pattern) {
        getElement().setProperty("pattern", pattern == null ? "" : pattern);
        getValidationSupport().setPattern(pattern);
    }

    /**
     * A regular expression that the value is checked against. The pattern must
     * match the entire value, not just some subset.
     *
     * @return the {@code pattern} property from the webcomponent
     */
    public String getPattern() {
        return getElement().getProperty("pattern");
    }

    /**
     * The text usually displayed in a tooltip popup when the mouse is over the
     * field.
     *
     * @return the {@code title} property from the webcomponent
     */
    public String getTitle() {
        return super.getTitleString();
    }

    @Override
    public void setTitle(String title) {
        super.setTitle(title);
    }

    /**
     * Specifies if the field value gets automatically selected when the field
     * gains focus.
     *
     * @return <code>true</code> if autoselect is active, <code>false</code>
     *         otherwise
     */
    public boolean isAutoselect() {
        return super.isAutoselectBoolean();
    }

    /**
     * Set to <code>true</code> to always have the field value automatically
     * selected when the field gains focus, <code>false</code> otherwise.
     *
     * @param autoselect
     *            <code>true</code> to set auto select on, <code>false</code>
     *            otherwise
     */
    @Override
    public void setAutoselect(boolean autoselect) {
        super.setAutoselect(autoselect);
    }

    @Override
    public String getEmptyValue() {
        return "";
    }

    /**
     * Sets the value of this email field. If the new value is not equal to
     * {@code getValue()}, fires a value change event. Throws
     * {@code NullPointerException}, if the value is null.
     * <p>
     * Note: {@link Binder} will take care of the {@code null} conversion when
     * integrates with email field, as long as no new converter is defined.
     *
     * @param value
     *            the new value, not {@code null}
     */
    @Override
    public void setValue(String value) {
        super.setValue(value);
    }

    /**
     * Returns the current value of the email field. By default, the empty email
     * field will return an empty string.
     *
     * @return the current value.
     */
    @Override
    public String getValue() {
        return super.getValue();
    }

    @Override
    public void setRequiredIndicatorVisible(boolean requiredIndicatorVisible) {
        super.setRequiredIndicatorVisible(requiredIndicatorVisible);
        getValidationSupport().setRequired(requiredIndicatorVisible);
    }

    @Override
    public Validator<String> getDefaultValidator() {
        return (value, context) -> getValidationSupport().checkValidity(value);
    }

    @Override
    public Registration addValidationStatusChangeListener(
            ValidationStatusChangeListener<String> listener) {
        return addClientValidatedEventListener(
                event -> listener.validationStatusChanged(
                        new ValidationStatusChangeEvent<String>(this,
                                !isInvalid())));
    }

    /**
     * Performs server-side validation of the current value. This is needed
     * because it is possible to circumvent the client-side validation
     * constraints using browser development tools.
     */
    @Override
    protected void validate() {
        setInvalid(getValidationSupport().isInvalid(getValue()));
    }

    @Override
    protected void onAttach(AttachEvent attachEvent) {
        super.onAttach(attachEvent);
        ClientValidationUtil.preventWebComponentFromModifyingInvalidState(this);
    }
}
