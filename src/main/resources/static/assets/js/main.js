const io = new IntersectionObserver(entries => {
    entries.forEach(entry => {
      if (entry.intersectionRatio > 0) {
        entry.target.classList.add('animated');
      }			
    })
  });

  // 감시 대상 선언, 감시(이벤트 발생)
const boxElList = document.querySelector('.box');
boxElList.forEach((el) => {
  io.observe(el);
});


/*
function updateIndicator(entries, observer) {
    const indicator = document.querySelector('.indicator');

    entries.forEach(entry => {
        const index = entry.target.textContent.replace('#', '');
        const el = indicator.querySelector(`[data-index="${index}"]`);    
        el.classList.toggle('on', entry.isIntersecting);
    });
}

const io = new IntersectionObserver(updateIndicator);

Array.from(document.querySelectorAll('.box')).forEach(box => {
    io.observe(box);
});
*/