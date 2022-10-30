const removeReadOnlyAndDisabledAttribute = (input) => {
    input.removeAttribute('disabled');
    input.removeAttribute('readonly');
};

const updateBtn = document.querySelector('.update-user-btn');

const inputGroup = {};
inputGroup['birth'] = document.querySelector('#birth');
inputGroup['gender'] = document.querySelector('#gender');
inputGroup['emailAuth'] = document.querySelector('#emailAuth');
inputGroup['role'] = document.querySelector('#role');
inputGroup['status'] = document.querySelector('#status');

updateBtn.addEventListener('click', (event) => {
    event.preventDefault();
    for (let key in inputGroup) {
        removeReadOnlyAndDisabledAttribute(inputGroup[key]);
    }

    updateBtn.outerHTML =
        '<button class="update-user-btn btn btn-primary p-2">수정 하기</button>';
});
