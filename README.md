# EnhancedLegacyText

An alternative input format for [Adventure](https://github.com/KyoriPowered/adventure), 
building on top of the well known [legacy formatting codes](https://minecraft.fandom.com/wiki/Formatting_codes). 

## The Format

### Color & Formatting

- [The legacy codes](https://minecraft.fandom.com/wiki/Formatting_codes)
- Adventure's hex format, (`&#<hex>` / `&#abc123`)

### Color Gradients

By default, surrounded by `{` and `}`, seperated by `,` (by default)

Examples: 
- `{&a,&2,&3}`
- `{&a,&#00aa00,&3}` (mix & match permitted)
- `{&#55ff55,&#00aa00,&#00aaaa}`

### Click & Hover events

Events are surrounded by `[` and `]`, and split at the first two `:`, first part being either `click` or `hover` (by default)

#### Click

Valid types: `open_url`, `run_command`, `suggest_command`, `change_page`, `copy_to_clipboard`

Examples:
- `[click:open_url:https://github.com/Vankka/EnhancedLegacyText]`
- `[click:run_command:say hello]`
- `[click:suggest_command:/help]`
- `[click:change_page:2]`
- `[click:copy_to_clipboard:Secret]`

#### Hover

Valid type: `show_text`

Examples:
- `[hover:show_text:Hello]`
- `[hover:show_text:&#00aa00Hello]`
