Place 3D model (.glb) files here.

- Add `earth.glb` at: `/static/models/earth.glb` (already downloaded by you).
- For other planets use lowercase filenames, e.g. `mars.glb`, `jupiter.glb`.

Notes:
- The frontend checks for the model via HEAD request to `/models/{planet}.glb` and will show a fallback image if not present.
- Do NOT place NASA API keys in this folder or anywhere in frontend code.
