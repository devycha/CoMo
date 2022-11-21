const removeReadonlyAttribute = (input) => {
    input.removeAttribute('readonly');
};

const updateBtn = document.querySelector('.update-user-btn');

const inputGroup = {};
inputGroup['birth'] = document.querySelector('#birth');
inputGroup['gender'] = document.querySelector('#gender');

updateBtn.addEventListener('click', (event) => {
    event.preventDefault();
    for (let key in inputGroup) {
        removeReadonlyAttribute(inputGroup[key]);
    }

    updateBtn.outerHTML =
        '<button class="update-user-btn btn btn-primary p-2">수정 하기</button>';
});
