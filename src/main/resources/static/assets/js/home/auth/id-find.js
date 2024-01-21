import useAxios from '/assets/js/api/useAxios.js'

window.onload = function() {
    const name = document.getElementById('username')
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
        handleSearchId()
        
    })

}

async function handleSearchId() {
    const name = document.getElementById('username').value
    const email = document.getElementById('email').value
    await useAxios.post('/api/v1/auth/find/id',
    {
        "username": name,
        "email": email
    }
    ,(res)=> {
        alert('찾으시는 아이디는 '+res.data+' 입니다.')
      
    },(err)=> {
        // console.log('err',err)
        alert(err.response.data.message)
    })
}