import { Component } from '@angular/core';
import { RouterModule } from '@angular/router';

@Component({
  selector: 'app-root',
  imports: [RouterModule],
  template: `
    <main>
        <header class="brand-name">
            <img class="brand-logo" src="./assets/mercado_libro.png" alt="Mercado Libro" />
        </header>
        <section class="content">
            <router-outlet></router-outlet>
        </section>
    </main>
  `,
  styleUrl: './app.component.css'
})
export class AppComponent {
  title = 'Mercado Libro';
}
