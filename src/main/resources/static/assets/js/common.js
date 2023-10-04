/* tab function */
function tab(usetab){
    let tab = document.getElementsByClassName(usetab)
    let tabWrap = document.querySelectorAll('.tab');
    let tabList = tab[0].querySelectorAll('li');
    let tabCon = tab[0].parentNode.querySelectorAll('.tab-con')
    
    function tabOff(){
        tabList.forEach(tabremove => {
            tabremove.classList.remove('on')
        })
        tabCon.forEach(tabconremove => {
            tabconremove.classList.remove('on')
        })
    }
    tabList.forEach( (tabLi,index) => {
        tabLi.addEventListener('click' ,function(e){
            let nodes = e.target.closest('.tab');
            let tabthis = e.target;
            
            if(e.target.classList.contains('on')){
                tabthis.classList.add('on');
            }else if(e.target.parentNode.classList.contains('on')){
                tabthis.parentNode.classList.add('on');
            }else if(e.target.classList.contains('on') == false ){
                tabOff()
                tabList[index].classList.add('on');
                tabCon[index].classList.add('on');
            }else if(e.target.parentNode.classList.contains('on') == false){
                tabOff()
                tabList[index].classList.add('on');
                tabCon[index].classList.add('on');
            }
        });
    });
}


window.onload = function(){}

    /*
    function selectCustom(){
        var labels = document.querySelectorAll('.dropdown');
        for (let index = 0; index < labels.length; index++) {
            let options = labels[index].querySelectorAll('.dropdown-toggle');
            let optionsList = labels[index].querySelectorAll('li');
            console.log(optionsList)
            labels[index].addEventListener('click', function(){
                if(labels[index].classList.contains('on')) {
                   labels[index].classList.remove('on');
                } else {
                    labels.forEach(function(lab){
                        lab.classList.remove('on');
                    })                    
                    labels[index].classList.add('on');
                }
            })
            labels[index].querySelectorAll('li').forEach(function(lablesLi){
                lablesLi.addEventListener('click',function(e){
                    let textVal = lablesLi.textContent;
                    lablesLi.parentNode.previousElementSibling.textContent = textVal
                })
            })
        }
    }selectCustom();
    */
    
    /* tab event */
    
