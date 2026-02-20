/*!
 * Color mode toggler for Bootstrap's docs (https://getbootstrap.com/)
 * Copyright 2011-2025 The Bootstrap Authors
 * Licensed under the Creative Commons Attribution 3.0 Unported License.
 */

(() => {
  'use strict'

  // Função para trocar o diretório das imagens temáticas
  const swapThemeImages = (theme) => {
    // Define o nome da pasta com base no tema
    const themeFolder = (theme === 'dark') ? 'dark-mode' : 'light-mode';
    
    // Seleciona todas as imagens que você quer alternar. 
    // DICA: Use uma classe específica, como 'theme-image-swap', para ser mais seguro.
    const imagesToSwap = document.querySelectorAll('.theme-image-swap'); 

    imagesToSwap.forEach(img => {
      const currentSrc = img.getAttribute('src');
      
      // Usa Regex para substituir o nome da pasta (light-mode ou dark-mode)
      // O regex busca por '/telas-app/' seguido por 'light-mode' OU 'dark-mode'
      const newSrc = currentSrc.replace(
        /(\/[a-z0-9_-]+\/)(light-mode|dark-mode)(\/.*)/, 
        `$1${themeFolder}$3`
      );
      
      // Aplica o novo caminho (newSrc)
      img.setAttribute('src', newSrc);
    });
  };

  const getStoredTheme = () => localStorage.getItem('theme')
  const setStoredTheme = theme => localStorage.setItem('theme', theme)

  const getPreferredTheme = () => {
    const storedTheme = getStoredTheme()
    if (storedTheme) {
      return storedTheme
    }

    return window.matchMedia('(prefers-color-scheme: dark)').matches ? 'dark' : 'light'
  }

  const setTheme = theme => {
    let finalTheme = theme;
    if (theme === 'auto') {
      finalTheme = (window.matchMedia('(prefers-color-scheme: dark)').matches ? 'dark' : 'light');
      document.documentElement.setAttribute('data-bs-theme', finalTheme);
    } else {
      document.documentElement.setAttribute('data-bs-theme', theme)
    }
    swapThemeImages(finalTheme);
  }

  setTheme(getPreferredTheme())

  const showActiveTheme = (theme, focus = false) => {
    const themeSwitcher = document.querySelector('#bd-theme')

    if (!themeSwitcher) {
      return
    }

    const themeSwitcherText = document.querySelector('#bd-theme-text')
    const activeThemeIcon = document.querySelector('.theme-icon-active use')
    const btnToActive = document.querySelector(`[data-bs-theme-value="${theme}"]`)
    const svgOfActiveBtn = btnToActive.querySelector('svg use').getAttribute('href')

    document.querySelectorAll('[data-bs-theme-value]').forEach(element => {
      element.classList.remove('active')
      element.setAttribute('aria-pressed', 'false')
    })

    btnToActive.classList.add('active')
    btnToActive.setAttribute('aria-pressed', 'true')
    activeThemeIcon.setAttribute('href', svgOfActiveBtn)
    const themeSwitcherLabel = `${themeSwitcherText.textContent} (${btnToActive.dataset.bsThemeValue})`
    themeSwitcher.setAttribute('aria-label', themeSwitcherLabel)

    if (focus) {
      themeSwitcher.focus()
    }
  }

  window.matchMedia('(prefers-color-scheme: dark)').addEventListener('change', () => {
    const storedTheme = getStoredTheme()
    if (storedTheme !== 'light' && storedTheme !== 'dark') {
      setTheme(getPreferredTheme())
    }
  })

  window.addEventListener('DOMContentLoaded', () => {
    showActiveTheme(getPreferredTheme())

    document.querySelectorAll('[data-bs-theme-value]')
      .forEach(toggle => {
        toggle.addEventListener('click', () => {
          const theme = toggle.getAttribute('data-bs-theme-value')
          setStoredTheme(theme)
          setTheme(theme)
          showActiveTheme(theme, true)
        })
      })
  })
})()