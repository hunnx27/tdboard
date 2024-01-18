window.onload = function() {
    const userId = document.getElementById('userId')
    const userName = document.getElementById('userName')
    const email = document.getElementById('email')
    const pwdFindConfirm = document.getElementById('pwdFindConfirm');
   
    userId.addEventListener('input', function() {
        if(userId.value && userName.value && email.value){
            pwdFindConfirm.disabled = false
        }else {
            pwdFindConfirm.disabled = true
        }
    })

    userName.addEventListener('input', function() {
        if(userId.value && userName.value && email.value){
            pwdFindConfirm.disabled = false
        }else {
            pwdFindConfirm.disabled = true
        }
    })
    email.addEventListener('input', function() {
        if(userId.value && userName.value && email.value){
            pwdFindConfirm.disabled = false
        }else {
            pwdFindConfirm.disabled = true
        }
    })

    pwdFindConfirm.addEventListener('click', function(){
        // api call
        alert('success')
    })

}
