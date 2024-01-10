$(function(){
    const termsCheck1 = document.getElementById('termsCheck1');
    const termsCheck2 = document.getElementById('termsCheck2');
    termsCheck1.addEventListener('change', handleChangeEvent);
    termsCheck2.addEventListener('change', handleChangeEvent);

});
function handleChangeEvent() {
    const termsCheck1 = document.getElementById('termsCheck1');
    const termsCheck2 = document.getElementById('termsCheck2');
    const nextButton = document.getElementById('nextButton');
    if (termsCheck1.checked && termsCheck2.checked) {
        nextButton.disabled = false;
    } else {
        nextButton.disabled = true;
    }
}
