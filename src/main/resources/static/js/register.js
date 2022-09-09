const validatePasswordInputInfo = (input) => {
    const invalidElement = document.createElement("div");
    invalidElement.classList.add("invalid-feedback");
    invalidElement.id = "invalid-password";

    const value = input.value;
    const regex = new RegExp(
        "^(?=.*[0-9])(?=.*[a-z])(?=.*[A-Z])(?=.*[!@#$%^&*(){}|_`´~\"',-./:;<>\\[\\]+?\\\\=]).{8,12}$"
    );

    if (!value || value === "") {
        invalidElement.textContent = "Password must be provided!";
        addElementAfter(invalidElement, input);
        input.classList.add("is-invalid");
    } else if (value.length < 8 || value.length > 12) {
        invalidElement.textContent = "Password must be between 8 and 12 characters!";
        addElementAfter(invalidElement, input);
        input.classList.add("is-invalid");
    } else if (!regex.test(value)) {
        invalidElement.textContent = "Password must include at least one uppercase and lowercase letters, " +
            "a number and a symbol with no white spaces";
        addElementAfter(invalidElement, input);
        input.classList.add("is-invalid");
    }

}

const validateUsernameInputInfo = (input, usernames) => {
    const invalidElement = document.createElement("div");
    invalidElement.classList.add("invalid-feedback");
    invalidElement.id = "invalid-username";

    const value = input.value;
    const regex = new RegExp(
        "^(?=.{5,20}$)(?![_.-])(?!.*[_.-]{2})[a-z0-9._-]+(?<![_.-])$"
    );

    if (!value || value === "") {
        invalidElement.textContent = "Username must be provided!";
        addElementAfter(invalidElement, input);
        input.classList.add("is-invalid");
    } else if (value.length < 5 || value.length > 20) {
        invalidElement.textContent = "Username must be between 5 and 20 characters!";
        addElementAfter(invalidElement, input);
        input.classList.add("is-invalid");
    } else if (!regex.test(value)) {
        invalidElement.textContent = "Username can only contain lower case letters, " +
            "numbers, underscore, dash or dot with no white spaces!";
        addElementAfter(invalidElement, input);
        input.classList.add("is-invalid");
    } else if (usernames.includes(value)) {
        invalidElement.textContent = "Username already taken!";
        addElementAfter(invalidElement, input);
        input.classList.add("is-invalid");
    }
}

const clearInputErrors = (input) => {
    if (input.classList.contains("is-invalid"))
        input.classList.remove("is-invalid");

    removeErrorElements(input);
}

const removeErrorElements = (element) => {
    for (let child of element.parentNode.children) {
        if (child.classList.contains("invalid-feedback"))
            element.parentNode.removeChild(child);
    }
}

const addElementAfter = (elementAfter, elementBefore) => {
    if (!document.body.contains(document.getElementById(elementAfter.id))) {
        elementBefore.parentNode.insertBefore(elementAfter, elementBefore.nextSibling);
    }
}

const sendInfo = (usernames) => {
    const usernameInput = document.getElementById("username");
    const passwordInput = document.getElementById("password");

    validateUsernameInputInfo(usernameInput, usernames);
    validatePasswordInputInfo(passwordInput);

    if (!checkErrors(usernameInput) && !checkErrors(passwordInput))
        document.forms["registerInfo"].submit();
}

const checkErrors = (element) => {
    for (let child of element.parentNode.children) {
        if (child.classList.contains("invalid-feedback"))
            return true;
    }

    return false;
}

