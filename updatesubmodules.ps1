git submodule update --init --recursive

$BASE=(Get-Location)

# update LS
cd LobbyService
git branch
git checkout master
git pull

cd $BASE
