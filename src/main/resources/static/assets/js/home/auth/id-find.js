window.onload = function() {
    const name = document.getElementById('name')
    const email = document.getElementById('email')
    const idFindConfirm = document.getElementById('idFindConfirm');
   
    name.addEventListener('input', function() {
        if(name.value && email.value){
            idFindConfirm.disabled = false
        }else {
            idFindConfirm.disabled = true
        }
    })
    email.addEventListener('input', function() {
        if(name.value && email.value){
            idFindConfirm.disabled = false
        }else {
            idFindConfirm.disabled = true
        }
    })

    idFindConfirm.addEventListener('click', function(){
        // api call
        alert('success')
    })

}
